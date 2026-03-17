package edu.kit.kastel;

import edu.kit.kastel.commands.CommandHandler;
import edu.kit.kastel.util.RecommendationSystem;

/**
 * The type main.
 *
 * @author uwing
 */
public final class Main {

    private Main() {
    }

    /**
     * The entry point of the application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

        RecommendationSystem recommendationSystem = new RecommendationSystem();

        CommandHandler commandHandler = new CommandHandler(recommendationSystem);
        commandHandler.runCommandHandler();
    }
}