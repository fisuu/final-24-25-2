package edu.kit.kastel.commands;

import edu.kit.kastel.RecommendationSystem;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * The type command handler.
 * This class is used to handle the commands of the program.
 *
 * @author uwing
 * @author Programmieren-Team
 */
public class CommandHandler {

    /**
     * The constant LOAD_ARGUMENT is used to check if an input is a load command.
     */
    public static final String LOAD_ARGUMENT = "load";
    /**
     * The constant DATABASE_ARGUMENT is used to check if an input is a load database command.
     */
    public static final String DATABASE_ARGUMENT = "database";
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String SPLITTER = " ";
    private static final String ERROR_MESSAGE = "Error, %s";
    private static final String UNKNOWN_COMMAND = "Unknown command: ";

    private boolean quitting;

    private final Map<String, Command> commands;
    private final RecommendationSystem recommendationSystem;


    /**
     * Instantiates a new command handler.
     *
     * @param recommendationSystem the recommendation system
     */
    public CommandHandler(RecommendationSystem recommendationSystem) {
        this.recommendationSystem = recommendationSystem;
        this.commands = new HashMap<>();
        this.quitting = false;
    }

    /**
     * Runs the command handler.
     */
    public void runCommandHandler() {

        initCommands();

        try (Scanner scanner = recommendationSystem.getScanner()) {
            while (!quitting && scanner.hasNext()) {

                String line = scanner.nextLine();
                String[] commandArgs = line.split(SPLITTER);
                String commandName = commandArgs[0];

                if (commandName.equals(LOAD_ARGUMENT) && commandArgs.length > 1 && commandArgs[1].equals(DATABASE_ARGUMENT)) {
                    commandName += Command.SEPARATOR + DATABASE_ARGUMENT;
                }

                if (!commands.containsKey(commandName) || !commandName.equals(QuitCommand.COMMAND_NAME)
                        && !commandName.equals(LOAD_ARGUMENT + SPLITTER + DATABASE_ARGUMENT)
                        && recommendationSystem.getGraph().getItemGraph().isEmpty()) {
                    System.err.printf(ERROR_MESSAGE + LINE_SEPARATOR, UNKNOWN_COMMAND + commandName);
                    continue;
                }

                try {
                    commands.get(commandName).execute(commandArgs);
                } catch (InvalidCommandArgumentException e) {
                    System.err.printf(ERROR_MESSAGE + LINE_SEPARATOR, e.getMessage());
                }
            }
        }
    }

    private void initCommands() {
        addCommand(new LoadDatabaseCommand(recommendationSystem));
        addCommand(new QuitCommand(this));
        addCommand(new ExportCommand(recommendationSystem));
        addCommand(new AddCommand(recommendationSystem));
        addCommand(new RemoveCommand(recommendationSystem));
        addCommand(new NodesCommand(recommendationSystem));
        addCommand(new EdgesCommand(recommendationSystem));
        addCommand(new RecommendCommand(recommendationSystem));
    }

    private void addCommand(Command command) {
        this.commands.put(command.getCommandName(), command);
    }

    /**
     * Quits the program.
     */
    public void quit() {
        quitting = true;
    }

}
