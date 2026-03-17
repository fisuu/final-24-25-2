package edu.kit.kastel.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

/**
 * The type graph.
 * Represents a graph with items and relations.
 * While items are the nodes and relations are the edges between the nodes.
 *
 * @author uwing
 */
public class Graph {

    private final Map<Item, List<Edge>> graph;

    /**
     * Instantiates a new graph.
     */
    public Graph() {
        this.graph = new HashMap<>();
    }

    /**
     * Gets the sorted directed graph.
     * The nodes are sorted by their name.
     * The edges are sorted by the name of the neighbor and the relation.
     *
     * @return the graph
     */
    public Map<Item, List<Edge>> getDirectedGraph() {
        Map<Item, List<Edge>> map = new TreeMap<>(Comparator.comparing(node -> node.getName()));
        map.putAll(graph);
        for (List<Edge> edges : map.values()) {
            edges.sort(Comparator.comparing((Edge edge) -> edge.getNeighbor().getName()).thenComparing(edge -> edge.getWeight().ordinal()));
        }
        return map;
    }

    /**
     * Resets the graph.
     */
    public void resetGraph() {
        graph.clear();
    }

    /**
     * Sets up the graph.
     * Used to restore the graph from a previous state.
     *
     * @param graph the old graph
     */
    public void setGraph(Map<Item, List<Edge>> graph) {
        this.graph.putAll(graph);
    }

    /**
     * Adds nodes and their relation to the graph.
     *
     * @param source   the source item
     * @param neighbor the neighbor item
     * @param weight   the relation between the two items
     */
    public void addToGraph(Item source, Item neighbor, Relation weight) {
        graph.putIfAbsent(source, new ArrayList<>());
        graph.putIfAbsent(neighbor, new ArrayList<>());
        if (!findEdge(source, neighbor, weight)) {
            graph.get(source).add(new Edge(neighbor, weight));
            graph.get(neighbor).add(new Edge(source, weight.reversalRelation()));
        }
    }

    /**
     * Removes an edge from the graph.
     * If a node is disconnected, it will be removed from the graph.
     *
     * @param source   the source item
     * @param neighbor the neighbor item
     * @param weight   the relation between the two items
     */
    public void removeEdge(Item source, Item neighbor, Relation weight) {
        if (findEdge(source, neighbor, weight)) {
            graph.get(source).removeIf(edge -> edge.getNeighbor().equals(neighbor) && edge.getWeight().equals(weight));
            graph.get(neighbor).removeIf(edge -> edge.getNeighbor().equals(source) && edge.getWeight().equals(weight.reversalRelation()));
            List<Item> disconnectedNodes = new ArrayList<>();
            for (Item node : graph.keySet()) {
                if (graph.get(node).isEmpty()) {
                    disconnectedNodes.add(node);
                }
            }
            for (Item node : disconnectedNodes) {
                graph.remove(node);
            }
        }
    }

    private boolean findEdge(Item source, Item neighbor, Relation weight) {
        for (Edge edge : graph.get(source)) {
            if (edge.getNeighbor().equals(neighbor) && edge.getWeight().equals(weight)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets siblings of a given item.
     * Siblings are items that share the same category they are contained in.
     *
     * @param node the item to get the siblings for
     * @return the siblings of the item
     */
    public Set<Item> getSiblings(Item node) {
        Set<Item> upperCategories = new HashSet<>();
        Set<Item> siblings = new HashSet<>();
        for (Edge edge : graph.get(node)) {
            if (edge.getWeight().equals(Relation.CONTAINED_IN_RELATION)) {
                upperCategories.add(edge.getNeighbor());
            }
        }
        if (upperCategories.isEmpty()) {
            return Collections.emptySet();
        }
        for (Item upperCategory : upperCategories) {
            for (Edge edge : graph.get(upperCategory)) {
                if (edge.getWeight().equals(Relation.CONTAINS_RELATION)) {
                    siblings.add(edge.getNeighbor());
                }
            }
        }
        siblings.removeAll(Collections.singletonList(node));
        return siblings;
    }

    /**
     * Gets the predecessors and all their predecessors of a given item.
     *
     * @param startNode the starting item
     * @return the set of predecessors
     */
    public Set<Item> getPredecessors(Item startNode) {
        Set<Item> nodes = breadthSearchGraph(startNode, Relation.SUCCESSOR_OF_RELATION);
        nodes.removeAll(Collections.singletonList(startNode));
        return nodes;
    }

    /**
     * Gets the successors and all their successors of a given item.
     *
     * @param startNode the starting item
     * @return the set of successors
     */
    public Set<Item> getSuccessors(Item startNode) {
        Set<Item> nodes = breadthSearchGraph(startNode, Relation.PREDECESSOR_OF_RELATION);
        nodes.removeAll(Collections.singletonList(startNode));
        return nodes;
    }

    private Set<Item> breadthSearchGraph(Item startNode, Relation relation) {
        Set<Item> visited = new HashSet<>();
        Queue<Item> queue = new LinkedList<>();
        queue.add(startNode);
        visited.add(startNode);

        while (!queue.isEmpty()) {
            Item currentNode = queue.poll();
            for (Edge edge : graph.get(currentNode)) {
                Item neighbor = edge.getNeighbor();
                if (!visited.contains(neighbor) && edge.getWeight().equals(relation)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        return visited;
    }
}
