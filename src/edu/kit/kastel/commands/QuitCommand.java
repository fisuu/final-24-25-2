package edu.kit.kastel.commands;

import edu.kit.kastel.util.RecommendationSystem;

/**
 * The type quit command.
 * The quit command is responsible for quitting the program.
 *
 * @author uwing
 */
public class QuitCommand extends Command {

    protected static final String COMMAND_NAME = "quit";

    private static final int ARG_LENGTH = 1;

    private final CommandHandler commandHandler;
    private final RecommendationSystem recommendationSystem;

    /**
     * Instantiates a new quit command.
     *
     * @param commandHandler       the command handler
     * @param recommendationSystem the recommendation system
     */
    public QuitCommand(CommandHandler commandHandler, RecommendationSystem recommendationSystem) {
        super(COMMAND_NAME);
        this.commandHandler = commandHandler;
        this.recommendationSystem = recommendationSystem;
    }

    @Override
    public String execute(String[] args) throws InvalidCommandArgumentException {

        validateArgumentLength(args, ARG_LENGTH);

        recommendationSystem.closeScanner();
        commandHandler.quit();
        return "";
    }
}
