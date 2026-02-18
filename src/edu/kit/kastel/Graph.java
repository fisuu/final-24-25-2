package edu.kit.kastel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * The type Graph.
 *
 * @author uwing
 */
public class Graph {

    private final Map<Item, List<Edge>> graph;
    private final Map<Item, List<Edge>> originalGraph;

    private int recursionCounter = 0;

    /**
     * Instantiates a new Graph.
     */
    public Graph() {
        this.graph = new HashMap<>();
        this.originalGraph = new HashMap<>();
    }

    /**
     * Gets graph.
     *
     * @return the graph
     */
    public Map<Item, List<Edge>> getItemGraph() {
        Map<Item, List<Edge>> map = new TreeMap<>(Comparator.comparing(node -> node.getName()));
        map.putAll(graph);
        for (List<Edge> edges : map.values()) {
            edges.sort(Comparator.comparing((Edge edge) -> edge.getNeighbor().getName()).thenComparing(edge -> edge.getWeight().ordinal()));
        }
        return map;
    }

    /**
     * Reset graph.
     */
    public void resetGraph() {
        graph.clear();
    }

    /**
     * Sets graph.
     *
     * @param graph the old graph
     */
    public void setGraph(Map<Item, List<Edge>> graph) {
        this.graph.putAll(graph);
    }

    /**
     * Sets original graph.
     **/
    public void setOriginalGraph() {
        this.originalGraph.putAll(graph);
    }


    /**
     * Add edge.
     *
     * @param source   the source
     * @param neighbor the neighbor
     * @param weight   the weight
     */
    public void addEdge(Item source, Item neighbor, Relation weight) {
        graph.putIfAbsent(source, new ArrayList<>());
        graph.putIfAbsent(neighbor, new ArrayList<>());
        if (!findEdge(source, neighbor, weight)) {
            graph.get(source).add(new Edge(neighbor, weight));
            graph.get(neighbor).add(new Edge(source, weight.reversalRelation()));
        }
    }

    /**
     * Remove edge.
     *
     * @param source   the source
     * @param neighbor the neighbor
     * @param weight   the weight
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
     * Gets neighbors.
     *
     * @param node the node
     * @return the neighbors
     */
    public List<Edge> getNeighbors(Item node) {
        return graph.get(node);
    }

    /**
     * Gets siblings.
     *
     * @param node the node
     * @return the siblings
     */
    public Set<Item> getSiblings(Item node) {
        Set<Item> upperCategories = new HashSet<>();
        Set<Item> siblings = new HashSet<>();
        for (Edge edge : getNeighbors(node)) {
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
        return siblings;
    }

    /**
     * Gets predecessors.
     *
     * @param node the node
     * @return the predecessors
     */
    public Set<Item> getPredecessors(Item node) {
        Set<Item> predecessors = new HashSet<>();
        for (Edge edge : graph.get(node)) {
            if (edge.getWeight().equals(Relation.SUCCESSOR_OF_RELATION)) {
                predecessors.add(edge.getNeighbor());
                recursionCounter++;
                if (recursionCounter > graph.size()) {
                    recursionCounter = 0;
                    return predecessors;
                }
            }
        }
        List<Item> earlierPredecessors = new ArrayList<>(predecessors);
        for (Item predecessor : earlierPredecessors) {
            predecessors.addAll(getPredecessors(predecessor));
        }
        return predecessors;
    }

    /**
     * Gets successors.
     *
     * @param node the node
     * @return the successors
     */
    public Set<Item> getSuccessors(Item node) {
        Set<Item> successors = new HashSet<>();
        for (Edge edge : graph.get(node)) {
            if (edge.getWeight().equals(Relation.PREDECESSOR_OF_RELATION)) {
                successors.add(edge.getNeighbor());
                recursionCounter++;
                if (recursionCounter > graph.size()) {
                    recursionCounter = 0;
                    return successors;
                }
            }
        }
        List<Item> laterSuccessors = new ArrayList<>(successors);
        for (Item successor : laterSuccessors) {
            successors.addAll(getSuccessors(successor));
        }
        return successors;
    }

    /**
     * Reset recursion counter.
     */
    public void resetRecursionCounter() {
        recursionCounter = 0;
    }
}
