package edu.kit.kastel.commands;

import edu.kit.kastel.graph.Item;
import edu.kit.kastel.util.RecommendationSystem;
import edu.kit.kastel.util.InvalidParsingException;
import edu.kit.kastel.util.FileReader;
import edu.kit.kastel.util.RecursiveDescentParser;
import java.util.ArrayList;
import java.util.List;

/**
 * The type recommend command.
 * This command is used to recommend products based on the input.
 *
 * @author uwing
 */
public class RecommendCommand extends Command {

    protected static final String COMMAND_NAME = "recommend";
    /**
     * The constant TERM_PATTERN is used to split the input into tokens.
     * While brackets and commas are split into separate tokens.
     */
    private static final String TERM_PATTERN = "(?=[(),])|(?<=[(),])|\\\\b(UNION|INTERSECTION)\\\\b";
    private static final String PRODUCT_OUTPUT_PATTERN = "%s:%d";

    private final RecommendationSystem recommendationSystem;

    /**
     * Instantiates a new recommendation command.
     *
     * @param recommendationSystem the recommendation system
     */
    protected RecommendCommand(RecommendationSystem recommendationSystem) {
        super(COMMAND_NAME);
        this.recommendationSystem = recommendationSystem;
    }

    @Override
    public String execute(String[] args) throws InvalidCommandArgumentException {

        // Since it's easier to apply a regex to the whole string, we concatenate the arguments
        StringBuilder input = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            input.append(args[i]).append(SEPARATOR);
        }
        List<String> line = new ArrayList<>(List.of(FileReader.cleanWhitespace(input.toString()).split(TERM_PATTERN)));

        RecursiveDescentParser parser = new RecursiveDescentParser(line, recommendationSystem);

        List<Item> recommendedProducts;
        try {
            recommendedProducts = sortList(parser.start());
        } catch (InvalidParsingException e) {
            throw new InvalidCommandArgumentException(e.getMessage());
        }
        StringBuilder output = new StringBuilder();
        for (Item product : recommendedProducts) {
            output.append(String.format(PRODUCT_OUTPUT_PATTERN + SEPARATOR, product.getName(), product.getId()));
        }
        if (!output.isEmpty()) {
            output.deleteCharAt(output.length() - FileReader.INDEX_OFFSET);
        }
        return output.toString();
    }

}
