package edu.kit.kastel.commands;

/**
 * The type invalid command argument exception.
 * This exception is thrown when the command argument is invalid.
 *
 * @author uwing
 * @author Programmieren-Team
 */
public class InvalidCommandArgumentException extends Exception {

    /**
     * Instantiates a new invalid command argument exception.
     *
     * @param cause the cause
     */
    public InvalidCommandArgumentException(String cause) {
        super(cause);
    }
}
