package edu.kit.kastel;

/**
 * The type Graph object.
 *
 * @author uwing
 */
public class Item {

    private final String name;
    private final int id;

    /**
     * Instantiates a new Graph object.
     *
     * @param name the name
     * @param id   the id
     */
    public Item(String name, int id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

}
