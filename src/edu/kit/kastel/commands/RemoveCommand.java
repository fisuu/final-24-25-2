package edu.kit.kastel.commands;

import edu.kit.kastel.RecommendationSystem;

/**
 * The type remove command.
 * this command is used to remove a relation between two nodes.
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
    public void execute(String[] args) throws InvalidCommandArgumentException {

        if (args.length < 4) {
            throw new InvalidCommandArgumentException(INVALID_SYNTAX.formatted(COMMAND_NAME));
        }

        EdgeValues edgeValues = getEdgeParameters(args, recommendationSystem);

        if (!existingEdge(edgeValues.source(), edgeValues.neighbour(), edgeValues.relation(), recommendationSystem)) {
            throw new InvalidCommandArgumentException(NOT_EXISTING_RELATION_MESSAGE);
        }

        recommendationSystem.getGraph().removeEdge(edgeValues.source(), edgeValues.neighbour(), edgeValues.relation());
    }
}
