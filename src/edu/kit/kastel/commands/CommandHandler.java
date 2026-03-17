package edu.kit.kastel.commands;

import edu.kit.kastel.util.RecommendationSystem;
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

    private static final String SPLITTER = " ";
    private static final String LOAD_ARGUMENT = "load";
    private static final String DATABASE_ARGUMENT = "database";
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String ERROR_MESSAGE = "Error, %s";
    private static final String UNKNOWN_COMMAND = "unknown command: ";
    private static final int COMMAND_NAME_ARGUMENTS_INDEX = 0;
    private static final int LOAD_DATABASE_COMMAND_LENGTH = 3;
    private static final int DATABASE_ARGUMENT_INDEX = 1;
    private static final String DATABASE_NOT_LOADED_MESSAGE = "load a database first.";

    private final Map<String, Command> commands;
    private final RecommendationSystem recommendationSystem;
    private boolean quitting;

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

                String commandName = identifyCommandName(commandArgs);

                if (checkCommand(commandName)) {
                    continue;
                }
                String result;
                try {
                    result = commands.get(commandName).execute(commandArgs);
                } catch (InvalidCommandArgumentException e) {
                    System.err.printf(ERROR_MESSAGE + LINE_SEPARATOR, e.getMessage());
                    continue;
                }
                if (!result.isEmpty() || commandName.equals(RecommendCommand.COMMAND_NAME)) {
                    System.out.println(result);
                }
            }
        }
    }

    private boolean checkCommand(String commandName) {
        if (!commands.containsKey(commandName)) {
            System.err.printf(ERROR_MESSAGE + LINE_SEPARATOR, UNKNOWN_COMMAND + commandName);
            return true;

        } else if (!commandName.equals(QuitCommand.COMMAND_NAME)
                && !commandName.equals(LOAD_ARGUMENT + SPLITTER + DATABASE_ARGUMENT)
                && recommendationSystem.getGraph().getDirectedGraph().isEmpty()) {
            System.err.printf(ERROR_MESSAGE + LINE_SEPARATOR, DATABASE_NOT_LOADED_MESSAGE);
            return true;
        }
        return false;
    }

    private static String identifyCommandName(String[] commandArgs) {
        String commandName = commandArgs[COMMAND_NAME_ARGUMENTS_INDEX];
        if (commandName.equals(LOAD_ARGUMENT) && commandArgs.length == LOAD_DATABASE_COMMAND_LENGTH
                && commandArgs[DATABASE_ARGUMENT_INDEX].equals(DATABASE_ARGUMENT)) {
            commandName += Command.SEPARATOR + DATABASE_ARGUMENT;
        }
        return commandName;
    }

    private void initCommands() {
        addCommand(new LoadDatabaseCommand(recommendationSystem));
        addCommand(new QuitCommand(this, recommendationSystem));
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
