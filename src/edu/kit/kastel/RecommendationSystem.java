package edu.kit.kastel;

import java.util.Scanner;

/**
 * The type Recommendation system.
 *
 * @author uwing
 */
public class RecommendationSystem {

    private final Graph graph;
    private final Scanner scanner;

    /**
     * Instantiates a new Recommendation system.
     */
    public RecommendationSystem() {
        this.graph = new Graph();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Gets graph.
     *
     * @return the graph
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Gets scanner.
     *
     * @return the scanner
     */
    public Scanner getScanner() {
        return scanner;
    }
}
