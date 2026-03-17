package edu.kit.kastel.commands;

import edu.kit.kastel.util.InvalidConfigurationException;
import edu.kit.kastel.util.EdgeParameters;
import edu.kit.kastel.util.FileReader;
import edu.kit.kastel.util.RecommendationSystem;


import static edu.kit.kastel.util.EdgeParameters.getEdgeParameters;


/**
 * The type add command.
 * This class is used for adding a new edge to the graph or rather a relation between two items.
 *
 * @author uwing
 */
public class AddCommand extends Command {

    /**
     * The constant MIN_ARGUMENTS_LENGTH is the minimum length of the arguments array.
     */
    public static final int MIN_ARGUMENTS_LENGTH = 4;

    private static final String COMMAND_NAME = "add";
    private static final String RELATION_EXISTS_MESSAGE = "relation already exists";
    private static final String NODE_NAME_OR_ID_EXISTS = "Item with %s exists";

    private final RecommendationSystem recommendationSystem;

    /**
     * Instantiates a new add command.
     *
     * @param recommendationSystem the recommendation system
     */
    protected AddCommand(RecommendationSystem recommendationSystem) {
        super(COMMAND_NAME);
        this.recommendationSystem = recommendationSystem;
    }

    @Override
    public String execute(String[] args) throws InvalidCommandArgumentException {

        if (args.length < MIN_ARGUMENTS_LENGTH) {
            throw new InvalidCommandArgumentException(INVALID_SYNTAX.formatted(COMMAND_NAME));
        }

        EdgeParameters edgeParameter = getEdgeParameters(args, recommendationSystem);

        if (existingEdge(edgeParameter.source(), edgeParameter.neighbour(), edgeParameter.relation(), recommendationSystem)) {
            throw new InvalidCommandArgumentException(RELATION_EXISTS_MESSAGE);
        }

        if (!edgeParameter.relation().validRelation(edgeParameter.source(), edgeParameter.neighbour())) {
            throw new InvalidCommandArgumentException(FileReader.INVALID_RELATION_MESSAGE);
        }

        try {
            FileReader.uniqueItems(recommendationSystem.getGraph().getDirectedGraph().keySet(), edgeParameter.source());
        } catch (InvalidConfigurationException e) {
            throw new InvalidCommandArgumentException(NODE_NAME_OR_ID_EXISTS.formatted(e.getMessage()));
        }

        recommendationSystem.getGraph().addToGraph(edgeParameter.source(), edgeParameter.neighbour(), edgeParameter.relation());

        return "";
    }
}
