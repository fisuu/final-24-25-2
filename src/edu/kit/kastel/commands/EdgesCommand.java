package edu.kit.kastel.commands;

import edu.kit.kastel.Edge;
import edu.kit.kastel.Item;
import edu.kit.kastel.RecommendationSystem;

/**
 * The type edges command.
 * This command is used to list all edges of the graph.
 *
 * @author uwing
 */
public class EdgesCommand extends Command {

    private static final String COMMAND_NAME = "edges";
    private static final String PRODUCT_SYNTAX = "%s:%d";
    private static final String EDGE_SYNTAX = "%s-[%s]->%s";
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private final RecommendationSystem recommendationSystem;

    /**
     * Instantiates a new edges command.
     *
     * @param recommendationSystem the recommendation system
     */
    protected EdgesCommand(RecommendationSystem recommendationSystem) {
        super(COMMAND_NAME);
        this.recommendationSystem = recommendationSystem;
    }

    @Override
    public void execute(String[] args) throws InvalidCommandArgumentException {

        invalidArgumentLength(args, 1);

        StringBuilder sb = new StringBuilder();

        for (Item object : recommendationSystem.getGraph().getItemGraph().keySet()) {
            for (Edge edge : recommendationSystem.getGraph().getItemGraph().get(object)) {
                String source;
                if (object.getId() != -1) {
                    source = PRODUCT_SYNTAX.formatted(object.getName(), object.getId());
                } else {
                    source = object.getName();
                }
                buildString(sb, edge, source);
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        System.out.println(sb);
    }

    private void buildString(StringBuilder sb, Edge edge, String source) {
        String target;
        if (edge.getNeighbor().getId() != -1) {
            target = PRODUCT_SYNTAX.formatted(edge.getNeighbor().getName(), edge.getNeighbor().getId());
            sb.append(String.format(EDGE_SYNTAX + LINE_SEPARATOR, source, edge.getWeight().getName(), target));
        } else {
            target = edge.getNeighbor().getName();
            sb.append(String.format(EDGE_SYNTAX + LINE_SEPARATOR, source, edge.getWeight().getName(), target));
        }
    }
}
