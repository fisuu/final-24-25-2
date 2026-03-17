package edu.kit.kastel.util;

import edu.kit.kastel.graph.Item;
import edu.kit.kastel.graph.Relation;
import edu.kit.kastel.commands.Command;
import edu.kit.kastel.commands.InvalidCommandArgumentException;
import java.util.HashSet;
import java.util.List;


import static edu.kit.kastel.util.ItemParameters.getItemParameters;

/**
 * The record edge parameters.
 * This record is used to store the parameters of an edge, such as the source item, the neighbour item and the relation.
 *
 * @param source    the source item
 * @param neighbour the related item
 * @param relation  the relation
 * @author uwing
 */
public record EdgeParameters(Item source, Item neighbour, Relation relation) {

    private static final String INVALID_RELATION_MESSAGE = "invalid relation.";

    /**
     * Gets all parameters to create or remove an edge.
     *
     * @param args                 the arguments to parse
     * @param recommendationSystem the recommendation system
     * @return the edge parameters as a record. (source, neighbour, relation)
     * @throws InvalidCommandArgumentException the invalid command argument exception
     */
    public static EdgeParameters getEdgeParameters(
            String[] args,
            RecommendationSystem recommendationSystem
    ) throws InvalidCommandArgumentException {

        List<String> line = getCleanArgs(args);

        ItemParameters itemParameters;
        try {
            itemParameters = getItemParameters(line);
        } catch (InvalidConfigurationException e) {
            throw new InvalidCommandArgumentException(e.getMessage());
        }

        Item source = FileReader.findItem(new HashSet<>(recommendationSystem.getGraph().getDirectedGraph().keySet()),
                itemParameters.sourceName(), itemParameters.sourceId());
        if (source == null) {
            source = new Item(itemParameters.sourceName(), itemParameters.sourceId(), itemParameters.subjectType());
        }

        Item neighbour = FileReader.findItem(new HashSet<>(recommendationSystem.getGraph().getDirectedGraph().keySet()),
                itemParameters.neighbourName(), itemParameters.neighbourId());
        if (neighbour == null) {
            neighbour = new Item(itemParameters.neighbourName(), itemParameters.neighbourId(), itemParameters.objectType());
        }

        Relation relation;
        try {
            relation = FileReader.getRelation(line);
        } catch (InvalidConfigurationException e) {
            throw new InvalidCommandArgumentException(INVALID_RELATION_MESSAGE);
        }
        return new EdgeParameters(source, neighbour, relation);
    }

    /**
     * Gets the arguments as a list of strings.
     * The first argument is the command name and is not needed.
     * The rest ist cleaned and returned as a list of strings.
     *
     * @param args the arguments
     * @return the cleaned arguments as a list of strings
     */
    private static List<String> getCleanArgs(String[] args) {
        StringBuilder input = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            input.append(args[i]).append(Command.SEPARATOR);
        }
        return FileReader.cleanLine(input.toString());
    }
}

