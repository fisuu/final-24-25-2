package edu.kit.kastel.commands;

import edu.kit.kastel.Edge;
import edu.kit.kastel.Item;
import edu.kit.kastel.InvalidConfigException;
import edu.kit.kastel.ReadFile;
import edu.kit.kastel.RecommendationSystem;
import edu.kit.kastel.Relation;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
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
     * The constant INVALID_ARGUMENTS is used when the amount of arguments is invalid.
     */
    protected static final String INVALID_ARGUMENTS = "invalid amount of arguments";
    /**
     * The constant INVALID_SYNTAX is used when the syntax is invalid.
     */
    protected static final String INVALID_SYNTAX = "invalid syntax. Use '%s <item> <relation> <item>'";
    protected static final String SEPARATOR = " ";

    private static final String INVALID_ITEM_MESSAGE = "invalid item(s).";
    private static final String INVALID_RELATION_MESSAGE = "invalid relation.";


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
     * @throws InvalidCommandArgumentException the invalid command argument exception
     */
    public abstract void execute(String[] args) throws InvalidCommandArgumentException;

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
    protected static void invalidArgumentLength(String[] args, int argLength) throws InvalidCommandArgumentException {
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
     *
     * @return true if the edge exists, false otherwise
     */
    protected boolean existingEdge(
            Item source,
            Item neighbour,
            Relation relation,
            RecommendationSystem recommendationSystem
    ) {
        List<Edge> edges = recommendationSystem.getGraph().getItemGraph().get(source);
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
     * Gets all parameters to create or remove an edge.
     *
     * @param args                 the arguments
     * @param recommendationSystem the recommendation system
     *
     * @return the edge parameters as a record. (source, neighbour, relation)
     *
     * @throws InvalidCommandArgumentException the invalid command argument exception
     */
    protected EdgeValues getEdgeParameters(
            String[] args,
            RecommendationSystem recommendationSystem
    ) throws InvalidCommandArgumentException {
        StringBuilder input = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            input.append(args[i]).append(SEPARATOR);
        }
        List<String> line = ReadFile.cleanArgs(input.toString());

        Item source = ReadFile.findObject(new HashSet<>(recommendationSystem.getGraph().getItemGraph().keySet()), line).get(0);
        if (source == null) {
            try {
                source = ReadFile.getSubject(line);
            } catch (InvalidConfigException e) {
                throw new InvalidCommandArgumentException(INVALID_ITEM_MESSAGE);
            }
        }

        Item neighbour = ReadFile.findObject(new HashSet<>(recommendationSystem.getGraph().getItemGraph().keySet()), line).get(1);
        if (neighbour == null) {
            try {
                neighbour = ReadFile.getObject(line);
            } catch (InvalidConfigException e) {
                throw new InvalidCommandArgumentException(INVALID_ITEM_MESSAGE);
            }
        }

        Relation relation;
        try {
            relation = ReadFile.getRelation(line);
        } catch (InvalidConfigException e) {
            throw new InvalidCommandArgumentException(INVALID_RELATION_MESSAGE);
        }
        return new EdgeValues(source, neighbour, relation);
    }

    /**
     * The record EdgeValues contains the source, neighbour and relation of an edge.
     *
     * @param source    the source item
     * @param neighbour the related item
     * @param relation  the relation
     */
    protected record EdgeValues(Item source, Item neighbour, Relation relation) {
    }

    /**
     * Sorts a list of nodes by their name.
     *
     * @param nodes the list of nodes
     *
     * @return the sorted list
     */
    protected List<Item> sortList(Set<Item> nodes) {
        List<Item> sortedList = new ArrayList<>(nodes);
        sortedList.sort(Comparator.comparing(node -> node.getName()));
        return sortedList;
    }
}
