package edu.kit.kastel.util;

import edu.kit.kastel.graph.Item;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * The type recursive descent parser.
 * This class is used to parse the input tokens and return a quantity of items based on the input.
 *
 * @author uwing
 */
public class RecursiveDescentParser {

    /**
     * The constant CLOSING_BRACKET is used to check for a closing bracket.
     */
    public static final String CLOSING_BRACKET = ")";
    /**
     * The constant OPENING_BRACKET is used to check for an opening bracket.
     */
    public static final String OPENING_BRACKET = "(";

    /**
     * The regex STRATEGY_PATTERN is used to check for a valid strategy.
     * It matches the pattern S[1-3].
     */
    private static final Pattern STRATEGY_PATTERN = Pattern.compile("^S[1-3]$");
    private static final String INVALID_TERM_SYNTAX_MESSAGE = "invalid term syntax.";
    private static final String UNION_KEYWORD = "UNION";
    private static final String INTERSECTION_KEYWORD = "INTERSECTION";
    private static final String INVALID_TERM_MESSAGE = "invalid term.";
    private static final String INVALID_ID_MESSAGE = "invalid item id.";
    private static final String INVALID_STRATEGY_INDEX_MESSAGE = "invalid strategy index.";
    private static final String INSIDE_BRACKET_TERM_SPLITTER = ",";
    private static final String ARGS_SPLITTER = " ";
    private static final char SIBLINGS_STRATEGY_INDEX = '1';
    private static final char SUCCESSORS_STRATEGY_INDEX = '2';
    private static final char PREDECESSORS_STRATEGY_INDEX = '3';
    private static final int STRATEGY_ARG_LENGTH = 2;
    private static final int TERM_TYPE_ARGUMENT_INDEX = 0;
    private static final int STRATEGY_TYPE_ARGUMENT_INDEX = 0;
    private static final int ITEM_ID_ARGUMENT_INDEX = 1;
    private static final int TERM_STRATEGY_TYPE_INDEX = 1;
    private static final int CURRENT_TOKEN_START_INDEX = 0;
    private static final String INVALID_SYNTAX_MESSAGE = "invalid syntax. Use 'recommend <term> | (UNION | INTERSECTION(<term>, <term>))'.";

    private final List<String> tokens;
    private final RecommendationSystem recommendationSystem;
    private int currentTokenIndex;

    /**
     * Instantiates a new recursive descent parser.
     *
     * @param tokens               the tokens
     * @param recommendationSystem the recommendation system
     */
    public RecursiveDescentParser(List<String> tokens, RecommendationSystem recommendationSystem) {
        this.tokens = new ArrayList<>(tokens);
        this.currentTokenIndex = CURRENT_TOKEN_START_INDEX;
        this.recommendationSystem = recommendationSystem;
    }

    /**
     * Entry point of the parser.
     * Here the parser starts to parse the input tokens and returns a list of items.
     *
     * @return the set of items that are recommended
     * @throws InvalidParsingException the parse exception
     */
    public Set<Item> start() throws InvalidParsingException {
        if (STRATEGY_PATTERN.matcher(tokens.get(currentTokenIndex).split(ARGS_SPLITTER)[TERM_TYPE_ARGUMENT_INDEX]).matches()) {
            String[] args = tokens.get(currentTokenIndex).split(ARGS_SPLITTER);
            if (args.length != STRATEGY_ARG_LENGTH) {
                throw new InvalidParsingException(INVALID_TERM_SYNTAX_MESSAGE);
            }
            currentTokenIndex++;
            return getStrategy(args[STRATEGY_TYPE_ARGUMENT_INDEX], getSourceNode(args[ITEM_ID_ARGUMENT_INDEX]));

        } else if (tokens.get(currentTokenIndex).equals(UNION_KEYWORD)) {
            currentTokenIndex++;
            match(OPENING_BRACKET);
            return uniteTerms();

        } else if (tokens.get(currentTokenIndex).equals(INTERSECTION_KEYWORD)) {
            currentTokenIndex++;
            match(OPENING_BRACKET);
            return intersectTerms();

        } else {
            throw new InvalidParsingException(INVALID_TERM_MESSAGE);
        }
    }

    private Set<Item> getStrategy(String strategy, Item sourceNode) throws InvalidParsingException {
        return switch (strategy.charAt(TERM_STRATEGY_TYPE_INDEX)) {
            case SIBLINGS_STRATEGY_INDEX -> recommendationSystem.getGraph().getSiblings(sourceNode);
            case SUCCESSORS_STRATEGY_INDEX -> recommendationSystem.getGraph().getSuccessors(sourceNode);
            case PREDECESSORS_STRATEGY_INDEX -> recommendationSystem.getGraph().getPredecessors(sourceNode);
            default -> throw new InvalidParsingException(INVALID_STRATEGY_INDEX_MESSAGE);
        };
    }

    private Set<Item> uniteTerms() throws InvalidParsingException {
        Set<Item> terms = new LinkedHashSet<>(start());
        match(INSIDE_BRACKET_TERM_SPLITTER);
        terms.addAll(start());
        match(CLOSING_BRACKET);
        return new HashSet<>(terms);
    }

    private Set<Item> intersectTerms() throws InvalidParsingException {
        Set<Item> terms = new LinkedHashSet<>(start());
        match(INSIDE_BRACKET_TERM_SPLITTER);
        terms.retainAll(start());
        match(CLOSING_BRACKET);
        return new HashSet<>(terms);
    }

    private void match(String exceptedToken) throws InvalidParsingException {
        if (currentTokenIndex < tokens.size() && tokens.get(currentTokenIndex).equals(exceptedToken)) {
            currentTokenIndex++;
        } else {
            throw new InvalidParsingException(INVALID_SYNTAX_MESSAGE);
        }
    }

    private Item getSourceNode(String sourceId) throws InvalidParsingException {
        int id;
        try {
            id = Integer.parseInt(sourceId);
        } catch (NumberFormatException e) {
            throw new InvalidParsingException(INVALID_ID_MESSAGE);
        }
        for (Item object : recommendationSystem.getGraph().getDirectedGraph().keySet()) {
            if (object.getId() == id) {
                return object;
            }
        }
        throw new InvalidParsingException(INVALID_ID_MESSAGE);
    }

}
