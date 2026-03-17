package edu.kit.kastel.util;

/**
 * The type invalid parsing exception.
 * This exception is thrown when the parsing of the input is invalid.
 *
 * @author uwing
 * @author Programmieren-Team
 */
public class InvalidParsingException extends Exception {

    /**
     * Instantiates a new invalid parsing exception.
     *
     * @param cause the cause
     */
    public InvalidParsingException(String cause) {
        super(cause);
    }
}
