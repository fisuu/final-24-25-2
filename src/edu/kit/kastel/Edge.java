package edu.kit.kastel;

/**
 * The type Edge.
 *
 * @author uwing
 */
public class Edge {

    private final Item neighbor;
    private final Relation weight;

    /**
     * Instantiates a new Edge.
     *
     * @param neighbor the neighbor
     * @param weight   the weight
     */
    public Edge(Item neighbor, Relation weight) {
        this.neighbor = neighbor;
        this.weight = weight;
    }

    /**
     * Gets neighbor.
     *
     * @return the neighbor
     */
    public Item getNeighbor() {
        return neighbor;
    }

    /**
     * Gets weight.
     *
     * @return the weight
     */
    public Relation getWeight() {
        return weight;
    }
}
