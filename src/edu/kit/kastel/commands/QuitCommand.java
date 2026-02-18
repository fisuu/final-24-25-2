package edu.kit.kastel.commands;

/**
 * The type quit command.
 * The quit command is responsible for quitting the program.
 *
 * @author uwing
 */
public class QuitCommand extends Command {

    /**
     * The constant COMMAND_NAME is the name of the command.
     */
    public static final String COMMAND_NAME = "quit";

    private static final int ARG_LENGTH = 1;

    private final CommandHandler commandHandler;

    /**
     * Instantiates a new quit command.
     *
     * @param commandHandler       the command handler
     */
    public QuitCommand(CommandHandler commandHandler) {
        super(COMMAND_NAME);
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) throws InvalidCommandArgumentException {

        invalidArgumentLength(args, ARG_LENGTH);

        commandHandler.quit();
    }
}
