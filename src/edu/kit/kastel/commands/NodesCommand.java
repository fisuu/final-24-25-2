package edu.kit.kastel.commands;

import edu.kit.kastel.Item;
import edu.kit.kastel.RecommendationSystem;

/**
 * The type nodes command.
 * This command is used to list all nodes in the graph.
 *
 * @author uwing
 */
public class NodesCommand extends Command {

    private static final String COMMAND_NAME = "nodes";
    private static final String PRODUCT_SYNTAX = "%s:%d";

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

    /**
     * Executes the command.
     *
     * @param args the args
     * @throws InvalidCommandArgumentException the invalid command argument exception
     */
    @Override
    public void execute(String[] args) throws InvalidCommandArgumentException {

        invalidArgumentLength(args, 1);

        StringBuilder sb = new StringBuilder();
        for (Item node : recommendationSystem.getGraph().getItemGraph().keySet()) {
            if (node.getId() != -1) {
                sb.append(String.format(PRODUCT_SYNTAX, node.getName(), node.getId())).append(SEPARATOR);
            } else if (node.getId() == -1) {
                sb.append(String.format(node.getName())).append(SEPARATOR);
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        System.out.println(sb);
    }
}
