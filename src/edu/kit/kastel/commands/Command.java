package edu.kit.kastel.commands;

import edu.kit.kastel.graph.Edge;
import edu.kit.kastel.graph.Item;
import edu.kit.kastel.util.RecommendationSystem;
import edu.kit.kastel.graph.Relation;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * The type command.
 * The command class is the parent class for all commands.
 *
 * @author uwing
 */
public abstract class Command {

    /**
     * The constant SEPARATOR is used to separate words.
     */
    public static final String SEPARATOR = " ";

    protected static final String INVALID_SYNTAX = "invalid syntax. Use '%s <item> <relation> <item>'";
    private static final String INVALID_ARGUMENTS = "invalid amount of arguments";

    private final String commandName;

    /**
     * Instantiates a new command.
     *
     * @param commandName the command name
     */
    protected Command(String commandName) {
        this.commandName = commandName;
    }

    /**
     * Executes the command.
     *
     * @param args the arguments
     * @return the output of the command. Empty string if no output is needed.
     * @throws InvalidCommandArgumentException the invalid command argument exception
     */
    public abstract String execute(String[] args) throws InvalidCommandArgumentException;

    /**
     * Gets the name of the command.
     *
     * @return the command name
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * Checks if the length of the arguments are valid.
     *
     * @param args      the arguments
     * @param argLength the needed length of the arguments
     * @throws InvalidCommandArgumentException the invalid command argument exception
     */
    protected static void validateArgumentLength(String[] args, int argLength) throws InvalidCommandArgumentException {
        if (args.length != argLength) {
            throw new InvalidCommandArgumentException(INVALID_ARGUMENTS);
        }
    }

    /**
     * Checks if the edge (already) exists.
     *
     * @param source               the source item
     * @param neighbour            the related item
     * @param relation             the relation
     * @param recommendationSystem the recommendation system
     * @return true if the edge exists, false otherwise
     */
    protected static boolean existingEdge(
            Item source,
            Item neighbour,
            Relation relation,
            RecommendationSystem recommendationSystem
    ) {
        List<Edge> edges = recommendationSystem.getGraph().getDirectedGraph().get(source);
        if (edges == null) {
            return false;
        }
        for (Edge edge : edges) {
            if (edge.getWeight().equals(relation) && edge.getNeighbor().equals(neighbour)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Sorts a list of nodes by their name.
     *
     * @param nodes the list of nodes
     * @return the sorted list
     */
    protected static List<Item> sortList(Set<Item> nodes) {
        List<Item> sorted = new ArrayList<>(nodes);
        sorted.sort(Comparator.comparing(node -> node.getName()));
        return sorted;
    }
}
