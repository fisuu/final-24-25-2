package edu.kit.kastel.commands;

import edu.kit.kastel.graph.Item;
import edu.kit.kastel.graph.ItemType;
import edu.kit.kastel.util.RecommendationSystem;

/**
 * The type nodes command.
 * This command is used to list all nodes in the graph.
 *
 * @author uwing
 */
public class NodesCommand extends Command {

    private static final String COMMAND_NAME = "nodes";
    private static final String PRODUCT_SYNTAX = "%s:%d";
    private static final int ARG_LENGTH = 1;

    private final RecommendationSystem recommendationSystem;

    /**
     * Instantiates a new nodes command.
     *
     * @param recommendationSystem the recommendation system
     */
    protected NodesCommand(RecommendationSystem recommendationSystem) {
        super(COMMAND_NAME);
        this.recommendationSystem = recommendationSystem;
    }

    @Override
    public String execute(String[] args) throws InvalidCommandArgumentException {

        validateArgumentLength(args, ARG_LENGTH);

        StringBuilder stringBuilder = new StringBuilder();
        for (Item node : recommendationSystem.getGraph().getDirectedGraph().keySet()) {
            if (node.getType().equals(ItemType.PRODUCT)) {
                stringBuilder.append(PRODUCT_SYNTAX.formatted(node.getName(), node.getId())).append(SEPARATOR);
            } else if (node.getType().equals(ItemType.CATEGORY)) {
                stringBuilder.append(String.format(node.getName())).append(SEPARATOR);
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - ARG_LENGTH);
        return stringBuilder.toString();
    }
}
