package edu.kit.kastel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The type Read file.
 *
 * @author uwing
 */
public final class ReadFile {

    /**
     * The constant INVALID_RELATION_MESSAGE.
     */
    public static final String INVALID_RELATION_MESSAGE = "Relation not allowed";

    private ReadFile() {
    }

    /**
     * Load file.
     *
     * @param path                 the path
     * @param recommendationSystem the recommendation system
     * @throws InvalidConfigException the invalid config exception
     */
    public static void loadFile(String path, RecommendationSystem recommendationSystem) throws InvalidConfigException {

        Set<Item> nodes = new HashSet<>();
        List<String> config;
        try {
            config = Files.readAllLines(Path.of(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new InvalidConfigException("Invalid path");
        }
        for (String line : config) {
            System.out.println(line);
        }
        for (String s : config) {
            List<String> line = cleanArgs(s);
            createGraph(line, nodes, recommendationSystem);
        }
        recommendationSystem.getGraph().setOriginalGraph();
    }

    /**
     * Clean args list.
     *
     * @param commandArgument the command argument
     * @return the list
     */
    public static List<String> cleanArgs(String commandArgument) {
        List<String> line = new ArrayList<>(Arrays.asList(commandArgument.split(" ")));
        for (int i = 0; i < line.size(); i++) {
            if (line.get(i).startsWith("(") && !line.get(i).contains(")")) {
                for (int j = i + 1; j < line.size(); j++) {
                    line.set(i, line.get(i) + line.get(j));
                    if (line.get(j).contains(")")) {
                        if (j >= i + 1) {
                            line.subList(i + 1, j + 1).clear();
                        }
                        break;
                    }
                }
            } else if (line.get(i).contains("(") && !line.get(i).startsWith("(") && !line.get(i).endsWith(")")) {
                for (char c : line.get(i).toCharArray()) {
                    if (c == '(') {
                        line.add(i + 1, line.get(i).substring(line.get(i).indexOf(c)));
                        line.set(i, line.get(i).substring(0, line.get(i).indexOf(c)));
                        if (!line.get(i).contains(")")) {
                            for (int j = i + 2; j < line.size(); j++) {
                                line.set(i + 1, line.get(i + 1) + line.get(j));
                                if (line.get(j).contains(")")) {
                                    if (j >= i + 2) {
                                        line.subList(i + 2, j + 1).clear();
                                    }
                                }

                            }
                        }
                    }
                }
            } else if (!line.get(i).startsWith("(") && line.get(i).contains(")") && line.get(i).endsWith(")")) {
                line.add(i + 1, line.get(i).substring(line.get(i).indexOf("("), line.get(i).lastIndexOf(")") + 1));
                line.set(i, line.get(i).substring(0, line.get(i).lastIndexOf("(")));
            }
        }
        return line;
    }

    private static void createGraph(
            List<String> line,
            Set<Item> objects,
            RecommendationSystem recommendationSystem
    ) throws InvalidConfigException {


        Item source = findObject(objects, line).get(0);

        if (source == null) {
            source = getSubject(line);
        }
        uniqueObject(objects, source);
        objects.add(source);

        Item neighbor = findObject(objects, line).get(1);

        if (neighbor == null) {
            neighbor = getObject(line);
        }

        uniqueObject(objects, neighbor);

        if (source.equals(neighbor)) {
            throw new InvalidConfigException("identical nodes");
        }

        Relation weight = getRelation(line);

        if (!weight.validRelation(source, neighbor)) {
            throw new InvalidConfigException(INVALID_RELATION_MESSAGE);
        }

        objects.add(neighbor);

        recommendationSystem.getGraph().addEdge(source, neighbor, weight);
    }

    /**
     * Gets relation.
     *
     * @param line the line
     * @return the relation
     * @throws InvalidConfigException the invalid config exception
     */
    public static Relation getRelation(List<String> line) throws InvalidConfigException {
        Relation weight;
        if (line.get(1).startsWith("(")) {
            weight = Relation.getRelation(line.get(2));
        } else {
            weight = Relation.getRelation(line.get(1));
        }
        if (weight == null) {
            throw new InvalidConfigException("invalid relation");
        }
        return weight;
    }

    /**
     * Gets subject.
     *
     * @param line the line
     * @return the subject
     * @throws InvalidConfigException the invalid config exception
     */
    public static Item getSubject(List<String> line) throws InvalidConfigException {
        Item source;
        if (line.get(1).startsWith("(")) {
            int id;
            try {
                id = Integer.parseInt(line.get(1).substring(4, line.get(1).length() - 1));
            } catch (NumberFormatException e) {
                throw new InvalidConfigException("invalid serial number");
            }
            source = new Item(line.get(0).toLowerCase(), id);
        } else {
            source = new Item(line.get(0).toLowerCase(), -1);
        }
        return source;
    }

    /**
     * Gets object.
     *
     * @param line the line
     * @return the object
     * @throws InvalidConfigException the invalid config exception
     */
    public static Item getObject(List<String> line) throws InvalidConfigException {
        Item neighbor;
        if (line.get(line.size() - 1).startsWith("(")) {
            int id;
            try {
                id = Integer.parseInt(line.get(line.size() - 1).substring(4, line.get(line.size() - 1).length() - 1));
            } catch (NumberFormatException e) {
                throw new InvalidConfigException("invalid serial number");
            }
            neighbor = new Item(line.get(line.size() - 2).toLowerCase(), id);
        } else {
            neighbor = new Item(line.get(line.size() - 1).toLowerCase(), -1);
        }
        return neighbor;
    }

    /**
     * Unique object boolean.
     *
     * @param objects the objects
     * @param object  the object
     * @return the boolean
     * @throws InvalidConfigException the invalid config exception
     */
    public static boolean uniqueObject(Set<Item> objects, Item object) throws InvalidConfigException {
        for (Item o : objects) {
            if (o.getName().equalsIgnoreCase(object.getName())) {
                if (o.getId() != object.getId() && object.getId() != -1) {
                    throw new InvalidConfigException("identical names");
                }
                return false;
            }
            if (object.getId() != -1 && o.getId() == object.getId() && !o.getName().equalsIgnoreCase(object.getName())) {
                throw new InvalidConfigException("identical serial numbers");
            }
        }
        return true;
    }

    /**
     * Find object node.
     *
     * @param objects the objects
     * @param line    the line
     * @return the node
     */
    public static List<Item> findObject(Set<Item> objects, List<String> line) {
        String name;
        int id;
        if (line.get(1).startsWith("(")) {
            name = line.get(0);
            id = Integer.parseInt(line.get(1).substring(4, line.get(1).length() - 1));
        } else {
            name = line.get(0);
            id = -1;
        }
        List<Item> nodes = new ArrayList<>();
        for (Item object : objects) {
            if (object.getName().equalsIgnoreCase(name) && object.getId() == id) {
                nodes.add(object);
                break;
            }
        }
        if (nodes.isEmpty()) {
            nodes.add(null);
        }
        if (line.get(line.size() - 1).startsWith("(")) {
            name = line.get(line.size() - 2);
            id = Integer.parseInt(line.get(line.size() - 1).substring(4, line.get(line.size() - 1).length() - 1));
        } else {
            name = line.get(line.size() - 1);
            id = -1;
        }
        for (Item object : objects) {
            if (object.getName().equalsIgnoreCase(name) && object.getId() == id) {
                nodes.add(object);
                break;
            }
        }
        if (nodes.size() == 1) {
            nodes.add(null);
        }
        return nodes;
    }
}
