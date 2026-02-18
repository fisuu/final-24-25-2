package edu.kit.kastel.commands;

import edu.kit.kastel.Edge;
import edu.kit.kastel.Item;
import edu.kit.kastel.InvalidConfigException;
import edu.kit.kastel.ReadFile;
import edu.kit.kastel.RecommendationSystem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type load database command.
 * The load database command is used to load a database from a file.
 *
 * @author uwing
 */
public class LoadDatabaseCommand extends Command {

    private static final String COMMAND_NAME = "load database";
    private static final String INVALID_CONFIG = "invalid configuration: %s";
    private static final int PATH_ARGUMENT_INDEX = 2;
    private static final int ARG_LENGTH = 3;

    private final RecommendationSystem recommendationSystem;

    /**
     * Instantiates a new load database command.
     *
     * @param recommendationSystem the recommendation system
     */
    public LoadDatabaseCommand(RecommendationSystem recommendationSystem) {
        super(COMMAND_NAME);
        this.recommendationSystem = recommendationSystem;
    }

    @Override
    public void execute(String[] args) throws InvalidCommandArgumentException {

        invalidArgumentLength(args, ARG_LENGTH);

        Map<Item, List<Edge>> oldGraph = new HashMap<>(recommendationSystem.getGraph().getItemGraph());

        try {
            recommendationSystem.getGraph().resetGraph();
            ReadFile.loadFile(args[PATH_ARGUMENT_INDEX], recommendationSystem);
        } catch (InvalidConfigException e) {
            recommendationSystem.getGraph().setGraph(oldGraph);
            throw new InvalidCommandArgumentException(String.format(INVALID_CONFIG, e.getMessage()));
        }
    }
}
