package edu.kit.kastel.commands;

import edu.kit.kastel.graph.Edge;
import edu.kit.kastel.graph.Item;
import edu.kit.kastel.graph.ItemType;
import edu.kit.kastel.util.RecommendationSystem;
import java.util.List;
import java.util.Map.Entry;

/**
 * The type export command.
 * This class is used for exporting the graph in a dot format.
 *
 * @author uwing
 */
public class ExportCommand extends Command {

    private static final String COMMAND_NAME = "export";
    private static final String PRINTED_GRAPH = "digraph {%s%s}";
    private static final String PRINTED_EDGE = "%s -> %s [label=%s]";
    private static final String PRINTED_NODE = "%s [shape=box]";
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String RELATION_CONCATENATION = "-";
    private static final String CONCATENATION_REPLACEMENT = "";
    private static final int ARG_LENGTH = 1;

    private final RecommendationSystem recommendationSystem;

    /**
     * Instantiates a new export command.
     *
     * @param recommendationSystem the recommendation system
     */
    protected ExportCommand(RecommendationSystem recommendationSystem) {
        super(COMMAND_NAME);
        this.recommendationSystem = recommendationSystem;
    }

    @Override
    public String execute(String[] args) throws InvalidCommandArgumentException {

        validateArgumentLength(args, ARG_LENGTH);

        StringBuilder graph = new StringBuilder();
        for (Entry<Item, List<Edge>> entry : recommendationSystem.getGraph().getDirectedGraph().entrySet()) {
            Item node = entry.getKey();
            for (Edge edge : entry.getValue()) {
                String relation = edge.getWeight().getName().replace(RELATION_CONCATENATION, CONCATENATION_REPLACEMENT);
                graph.append(String.format(PRINTED_EDGE + LINE_SEPARATOR, node.getName(), edge.getNeighbor().getName(), relation));
            }
        }

        for (Entry<Item, List<Edge>> entry : recommendationSystem.getGraph().getDirectedGraph().entrySet()) {
            Item node = entry.getKey();
            if (node.getType().equals(ItemType.CATEGORY)) {
                graph.append(String.format(PRINTED_NODE + LINE_SEPARATOR, node.getName()));
            }
        }
        return PRINTED_GRAPH.formatted(LINE_SEPARATOR, graph);
    }
}
