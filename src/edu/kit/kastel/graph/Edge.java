package edu.kit.kastel.graph;

/**
 * The type edge.
 * Represents an edge in the graph.
 *
 * @author uwing
 */
public class Edge {

    private final Item neighbor;
    private final Relation weight;

    /**
     * Instantiates a new edge.
     *
     * @param neighbor the neighbor item that is connected by the edge
     * @param weight   the relation between the two items
     */
    public Edge(Item neighbor, Relation weight) {
        this.neighbor = neighbor;
        this.weight = weight;
    }

    /**
     * Gets the neighbor.
     *
     * @return the neighbor item
     */
    public Item getNeighbor() {
        return neighbor;
    }

    /**
     * Gets the weight.
     *
     * @return the relation between the two items
     */
    public Relation getWeight() {
        return weight;
    }
}
