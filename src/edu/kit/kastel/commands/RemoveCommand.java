package edu.kit.kastel.commands;

import edu.kit.kastel.util.RecommendationSystem;
import edu.kit.kastel.util.EdgeParameters;


import static edu.kit.kastel.util.EdgeParameters.getEdgeParameters;

/**
 * The type remove command.
 * This command is used to remove a relation(edge) between two nodes.
 * If a node has no more relations, it will be removed from the graph.
 *
 * @author uwing
 */
public class RemoveCommand extends Command {

    private static final String COMMAND_NAME = "remove";
    private static final String NOT_EXISTING_RELATION_MESSAGE = "relation does not exist";

    private final RecommendationSystem recommendationSystem;

    /**
     * Instantiates a new remove command.
     *
     * @param recommendationSystem the recommendation system
     */
    public RemoveCommand(RecommendationSystem recommendationSystem) {
        super(COMMAND_NAME);
        this.recommendationSystem = recommendationSystem;
    }

    @Override
    public String execute(String[] args) throws InvalidCommandArgumentException {

        if (args.length < AddCommand.MIN_ARGUMENTS_LENGTH) {
            throw new InvalidCommandArgumentException(INVALID_SYNTAX.formatted(COMMAND_NAME));
        }

        EdgeParameters edgeParameter = getEdgeParameters(args, recommendationSystem);

        if (!existingEdge(edgeParameter.source(), edgeParameter.neighbour(), edgeParameter.relation(), recommendationSystem)) {
            throw new InvalidCommandArgumentException(NOT_EXISTING_RELATION_MESSAGE);
        }

        recommendationSystem.getGraph().removeEdge(edgeParameter.source(), edgeParameter.neighbour(), edgeParameter.relation());
        return "";
    }
}
