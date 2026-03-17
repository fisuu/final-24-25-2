package edu.kit.kastel.commands;

import edu.kit.kastel.graph.Edge;
import edu.kit.kastel.graph.Item;
import edu.kit.kastel.util.InvalidConfigurationException;
import edu.kit.kastel.util.FileReader;
import edu.kit.kastel.util.RecommendationSystem;
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
    private static final int PATH_ARGUMENT_INDEX = 2;

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
    public String execute(String[] args) throws InvalidCommandArgumentException {

        Map<Item, List<Edge>> oldGraph = new HashMap<>(recommendationSystem.getGraph().getDirectedGraph());

        recommendationSystem.getGraph().resetGraph();

        try {
            FileReader.loadFile(args[PATH_ARGUMENT_INDEX], recommendationSystem);
        } catch (InvalidConfigurationException e) {
            recommendationSystem.getGraph().setGraph(oldGraph);
            throw new InvalidCommandArgumentException(e.getMessage());
        }
        return "";

    }
}
