package edu.kit.kastel.graph;

/**
 * The type item.
 * Represents an item in the graph.
 *
 * @author uwing
 */
public class Item {

    private final String name;
    private final int id;
    private final ItemType type;

    /**
     * Instantiates a new item.
     *
     * @param name the name of the item
     * @param id   the id of the item
     * @param type the type of the item
     */
    public Item(String name, int id, ItemType type) {
        this.name = name;
        this.id = id;
        this.type = type;
    }

    /**
     * Gets the name of the item.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the id of the item.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the type of the item.
     *
     * @return the type
     */
    public ItemType getType() {
        return type;
    }

}
