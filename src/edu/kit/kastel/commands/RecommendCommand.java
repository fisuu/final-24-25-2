package edu.kit.kastel.commands;

import edu.kit.kastel.CleanUpArguments;
import edu.kit.kastel.Item;
import edu.kit.kastel.RecommendationSystem;
import edu.kit.kastel.RecursiveDescentParser;
import java.util.List;

/**
 * The type recommend command.
 * This command is used to recommend products based on the input.
 *
 * @author uwing
 */
public class RecommendCommand extends Command {

    private static final String COMMAND_NAME = "recommend";

    private static final String PRODUCT_PATTERN = "%s:%d";

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

    /**
     * Executes the command.
     *
     * @param args the arguments
     * @throws InvalidCommandArgumentException the invalid command argument exception
     */
    @Override
    public void execute(String[] args) throws InvalidCommandArgumentException {

        StringBuilder input = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            input.append(args[i]).append(SEPARATOR);
        }
        List<String> line = CleanUpArguments.cleanTerm(input.toString());

        RecursiveDescentParser parser = new RecursiveDescentParser(line, recommendationSystem);

        List<Item> recommendedProducts = sortList(parser.start());
        StringBuilder output = new StringBuilder();
        for (Item product : recommendedProducts) {
            output.append(String.format(PRODUCT_PATTERN + SEPARATOR, product.getName(), product.getId()));
        }
        if (!output.isEmpty()) {
            output.deleteCharAt(output.length() - 1);
        }
        System.out.println(output);
    }

}
