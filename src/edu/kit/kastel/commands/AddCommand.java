package edu.kit.kastel.commands;

import edu.kit.kastel.InvalidConfigException;
import edu.kit.kastel.ReadFile;
import edu.kit.kastel.RecommendationSystem;

/**
 * The type add command.
 * This class is used for adding a new edge to the graph or rather a relation between two items.
 *
 * @author uwing
 */
public class AddCommand extends Command {

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
    public void execute(String[] args) throws InvalidCommandArgumentException {

        if (args.length < 4) {
            throw new InvalidCommandArgumentException(INVALID_SYNTAX.formatted(COMMAND_NAME));
        }

        EdgeValues edgeValues = getEdgeParameters(args, recommendationSystem);

        if (existingEdge(edgeValues.source(), edgeValues.neighbour(), edgeValues.relation(), recommendationSystem)) {
            throw new InvalidCommandArgumentException(RELATION_EXISTS_MESSAGE);
        }

        if (!edgeValues.relation().validRelation(edgeValues.source(), edgeValues.neighbour())) {
            throw new InvalidCommandArgumentException(ReadFile.INVALID_RELATION_MESSAGE);
        }

        try {
            ReadFile.uniqueObject(recommendationSystem.getGraph().getItemGraph().keySet(), edgeValues.source());
        } catch (InvalidConfigException e) {
            throw new InvalidCommandArgumentException(NODE_NAME_OR_ID_EXISTS.formatted(e.getMessage()));
        }

        recommendationSystem.getGraph().addEdge(edgeValues.source(), edgeValues.neighbour(), edgeValues.relation());
    }
}
