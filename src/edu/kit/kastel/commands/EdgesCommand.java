package edu.kit.kastel.commands;

import edu.kit.kastel.graph.Edge;
import edu.kit.kastel.graph.Item;
import edu.kit.kastel.graph.ItemType;
import edu.kit.kastel.util.RecommendationSystem;
import edu.kit.kastel.util.FileReader;

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
    private static final int ARG_LENGTH = 1;

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
    public String execute(String[] args) throws InvalidCommandArgumentException {

        validateArgumentLength(args, ARG_LENGTH);

        StringBuilder output = new StringBuilder();

        for (Item object : recommendationSystem.getGraph().getDirectedGraph().keySet()) {
            for (Edge edge : recommendationSystem.getGraph().getDirectedGraph().get(object)) {
                String source;
                if (object.getType().equals(ItemType.PRODUCT)) {
                    source = PRODUCT_SYNTAX.formatted(object.getName(), object.getId());
                } else {
                    source = object.getName();
                }
                buildString(output, edge, source);
            }
        }
        output.deleteCharAt(output.length() - FileReader.INDEX_OFFSET);
        return output.toString();
    }

    private static void buildString(StringBuilder stringBuilder, Edge edge, String source) {
        String target;
        if (edge.getNeighbor().getType().equals(ItemType.PRODUCT)) {
            target = PRODUCT_SYNTAX.formatted(edge.getNeighbor().getName(), edge.getNeighbor().getId());
            stringBuilder.append(String.format(EDGE_SYNTAX + LINE_SEPARATOR, source, edge.getWeight().getName(), target));
        } else {
            target = edge.getNeighbor().getName();
            stringBuilder.append(String.format(EDGE_SYNTAX + LINE_SEPARATOR, source, edge.getWeight().getName(), target));
        }
    }
}
