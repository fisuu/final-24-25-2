package edu.kit.kastel.util;

import edu.kit.kastel.graph.Item;
import edu.kit.kastel.graph.ItemType;
import edu.kit.kastel.graph.Relation;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import static edu.kit.kastel.util.ItemParameters.getItemParameters;

/**
 * The type file reader.
 * The file reader class is used to read the configuration file and create the graph.
 *
 * @author uwing
 */
public final class FileReader {

    /**
     * The constant INVALID_RELATION_MESSAGE is used to indicate an invalid relation.
     */
    public static final String INVALID_RELATION_MESSAGE = "invalid relation";
    /**
     * The constant INVALID_SERIAL_NUMBER_MESSAGE is used to indicate an invalid serial number.
     */
    public static final String INVALID_SERIAL_NUMBER_MESSAGE = "invalid serial number";
    /**
     * The constant INDEX_OFFSET is used to adjust the index.
     * E.g. used to get an element from a list that starts at 0.
     */
    public static final int INDEX_OFFSET = 1;

    /**
     * The regex CLEAN_UP_WHITE_SPACES_PATTERN is used to clean the whitespaces from a string.
     * Such as whitespaces before and after brackets, commas and whitespaces.
     */
    private static final String CLEAN_UP_WHITE_SPACES_PATTERN = "\\s*(\\(|\\)|,|\\s)\\s*";
    private static final String WHITE_SPACE_REPLACEMENT = "$1";

    private static final String ITEMS_SPLITTER = "(?=[(),])|(?<=[(),])|\\s+";
    private static final String INVALID_PATH_MESSAGE = "invalid path";
    private static final String IDENTICAL_NODE_RELATION_MESSAGE = "can't relate identical nodes";
    private static final String IDENTICAL_NAMES_MESSAGE = "identical names";
    private static final String IDENTICAL_SERIAL_NUMBERS_MESSAGE = "identical serial numbers";
    private static final char WORD_SEPARATOR = ' ';
    private static final char OPENING_BRACKET = '(';
    private static final char CLOSING_BRACKET = ')';
    private static final int RELATION_LINE_INDEX_IF_SUBJECT_HAS_ID = 4;
    private static final int RELATION_LINE_INDEX = 1;
    private static final int ID_OPENING_BRACKET_LINE_INDEX = 1;
    private static final int LOOP_NEXT_INDEX = 1;

    private FileReader() {
    }

    /**
     * The load file method reads the configuration file and creates the graph.
     *
     * @param path                 the configuration file path
     * @param recommendationSystem the recommendation system
     * @throws InvalidConfigurationException the invalid config exception
     */
    public static void loadFile(String path, RecommendationSystem recommendationSystem) throws InvalidConfigurationException {

        Set<Item> nodes = new HashSet<>();
        List<String> config;
        try {
            config = Files.readAllLines(Path.of(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new InvalidConfigurationException(INVALID_PATH_MESSAGE);
        }

        // Printing here instead of in the command handler, since the program still operates afterwards
        for (String line : config) {
            System.out.println(line);
        }
        for (String line : config) {
            createGraph(cleanLine(line), nodes, recommendationSystem);
        }
    }

    /**
     * Cleans and separates the command argument from brackets and whitespaces.
     *
     * @param input the input string
     * @return list of tokens
     */
    public static List<String> cleanLine(String input) {
        String cleanedInput = input;

        for (int i = 0; i < cleanedInput.length(); i++) {
            if (cleanedInput.charAt(i) == OPENING_BRACKET) {
                StringBuilder id = new StringBuilder();
                int charIndex = i + LOOP_NEXT_INDEX;
                for (char character : cleanedInput.substring(charIndex).toCharArray()) {
                    if (character == CLOSING_BRACKET) {
                        break;
                    } else if (character != WORD_SEPARATOR) {
                        id.append(character);
                    }
                    charIndex++;
                }
                cleanedInput = cleanedInput.substring(0, i + LOOP_NEXT_INDEX) + id + cleanedInput.substring(charIndex);
            }
        }
        cleanedInput = cleanWhitespace(cleanedInput);
        List<String> result = new ArrayList<>(Arrays.asList(cleanedInput.split(ITEMS_SPLITTER)));
        result.removeAll(Arrays.asList("", null));

        return result;
    }

    private static void createGraph(
            List<String> line,
            Set<Item> items,
            RecommendationSystem recommendationSystem
    ) throws InvalidConfigurationException {

        ItemParameters itemParameters = getItemParameters(line);

        if (itemParameters.sourceName().equalsIgnoreCase(itemParameters.neighbourName())) {
            throw new InvalidConfigurationException(IDENTICAL_NODE_RELATION_MESSAGE);
        }

        Item source = findItem(items, itemParameters.sourceName(), itemParameters.sourceId());

        if (source == null) {
            source = new Item(itemParameters.sourceName(), itemParameters.sourceId(), itemParameters.subjectType());
        }

        Item neighbor = findItem(items, itemParameters.neighbourName(), itemParameters.neighbourId());

        if (neighbor == null) {
            neighbor = new Item(itemParameters.neighbourName(), itemParameters.neighbourId(), itemParameters.objectType());
        }

        uniqueItems(items, source);
        uniqueItems(items, neighbor);

        items.add(source);
        items.add(neighbor);

        Relation weight = getRelation(line);

        if (!weight.validRelation(source, neighbor)) {
            throw new InvalidConfigurationException(INVALID_RELATION_MESSAGE);
        }

        recommendationSystem.getGraph().addToGraph(source, neighbor, weight);
    }


    /**
     * Gets the relation.
     *
     * @param line the configuration tokens
     * @return the relation if it exists
     * @throws InvalidConfigurationException the invalid config exception
     */
    public static Relation getRelation(List<String> line) throws InvalidConfigurationException {
        Relation weight;
        if (line.get(ID_OPENING_BRACKET_LINE_INDEX).equals(Character.toString(OPENING_BRACKET))) {
            weight = Relation.getRelation(line.get(RELATION_LINE_INDEX_IF_SUBJECT_HAS_ID));
        } else {
            weight = Relation.getRelation(line.get(RELATION_LINE_INDEX));
        }
        if (weight == null) {
            throw new InvalidConfigurationException(INVALID_RELATION_MESSAGE);
        }
        return weight;
    }

    /**
     * Checks if a given item is unique.
     *
     * @param nodes the list of items already in the graph
     * @param item  the item
     * @throws InvalidConfigurationException the invalid config exception
     */
    public static void uniqueItems(Set<Item> nodes, Item item) throws InvalidConfigurationException {
        for (Item node : nodes) {
            if (node.getName().equalsIgnoreCase(item.getName()) && (node.getId() != item.getId()
                    && item.getType().equals(ItemType.PRODUCT))) {
                throw new InvalidConfigurationException(IDENTICAL_NAMES_MESSAGE);
            }
            if (item.getType().equals(ItemType.PRODUCT) && node.getId() == item.getId()
                    && !node.getName().equalsIgnoreCase(item.getName())) {
                throw new InvalidConfigurationException(IDENTICAL_SERIAL_NUMBERS_MESSAGE);
            }
        }
    }

    /**
     * Searches for an item in the graph.
     *
     * @param nodes the nodes in the graph
     * @param name  the name of the new item
     * @param id    the id of the new item
     * @return the item if it exists, otherwise null
     */
    public static Item findItem(Set<Item> nodes, String name, int id) {
        for (Item item : nodes) {
            if (item.getName().equalsIgnoreCase(name) && item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    /**
     * Cleans the whitespaces from a given string.
     *
     * @param input the input string
     * @return the cleaned string
     */
    public static String cleanWhitespace(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll(CLEAN_UP_WHITE_SPACES_PATTERN, WHITE_SPACE_REPLACEMENT).trim();
    }
}
