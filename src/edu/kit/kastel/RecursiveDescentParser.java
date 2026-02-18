package edu.kit.kastel;

import edu.kit.kastel.commands.InvalidCommandArgumentException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * The type Recursive descent parser.
 *
 * @author uwing
 */
public class RecursiveDescentParser {

    private final List<String> tokens;
    private final RecommendationSystem recommendationSystem;
    private int currentTokenIndex;

    /**
     * Instantiates a new Recursive descent parser.
     *
     * @param tokens               the tokens
     * @param recommendationSystem the recommendation system
     */
    public RecursiveDescentParser(List<String> tokens, RecommendationSystem recommendationSystem) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
        this.recommendationSystem = recommendationSystem;
    }

    /**
     * Start list.
     *
     * @return the list
     * @throws InvalidCommandArgumentException the invalid command argument exception
     */
    public Set<Item> start() throws InvalidCommandArgumentException {
        if (tokens.get(currentTokenIndex).startsWith("S")) {
            String[] args = tokens.get(currentTokenIndex).split(" ");
            if (args.length > 2) {
                throw new InvalidCommandArgumentException("Invalid token: " + args[2]);
            }
            currentTokenIndex++;
            return getStrategy(args[0], sourceNode(args[1]));
        } else if (tokens.get(currentTokenIndex).equals("UNION")) {
            currentTokenIndex++;
            match("(");
            return uniteTerms();
        } else if (tokens.get(currentTokenIndex).equals("INTERSECTION")) {
            currentTokenIndex++;
            match("(");
            return intersectTerms();
        } else {
            throw new InvalidCommandArgumentException("Invalid token: " + tokens.get(currentTokenIndex));
        }
    }

    private Set<Item> getStrategy(String strategy, Item sourceNode) throws InvalidCommandArgumentException {
        switch (strategy) {
            case "S1" -> {
                Set<Item> recommendedProducts = recommendationSystem.getGraph().getSiblings(sourceNode);
                recommendedProducts.removeAll(Collections.singletonList(sourceNode));
                return recommendedProducts;
            }
            case "S2" -> {
                recommendationSystem.getGraph().resetRecursionCounter();
                Set<Item> recommendedProducts = recommendationSystem.getGraph().getSuccessors(sourceNode);
                recommendedProducts.removeAll(Collections.singletonList(sourceNode));
                return recommendedProducts;
            }
            case "S3" -> {
                recommendationSystem.getGraph().resetRecursionCounter();
                Set<Item> recommendedProducts = recommendationSystem.getGraph().getPredecessors(sourceNode);
                recommendedProducts.removeAll(Collections.singletonList(sourceNode));
                return recommendedProducts;
            }
            default -> throw new InvalidCommandArgumentException("invalid strategy index");
        }
    }

    private Set<Item> uniteTerms() throws InvalidCommandArgumentException {
        Set<Item> terms = new LinkedHashSet<>(start());
        terms.addAll(start());
        match(")");
        return new HashSet<>(terms);
    }

    private Set<Item> intersectTerms() throws InvalidCommandArgumentException {
        Set<Item> terms = new LinkedHashSet<>(start());
        terms.retainAll(start());
        match(")");
        return new HashSet<>(terms);
    }

    private void match(String exceptedToken) {
        if (tokens.get(currentTokenIndex).equals(exceptedToken)) {
            currentTokenIndex++;
        } else {
            throw new IllegalArgumentException("Invalid token: " + tokens.get(currentTokenIndex));
        }
    }

    private Item sourceNode(String sourceId) throws InvalidCommandArgumentException {
        int id;
        try {
            id = Integer.parseInt(sourceId);
        } catch (NumberFormatException e) {
            throw new InvalidCommandArgumentException("invalid source id");
        }
        for (Item object : recommendationSystem.getGraph().getItemGraph().keySet()) {
            if (object.getId() == id) {
                return object;
            }
        }
        throw new InvalidCommandArgumentException("invalid source id");
    }

}
