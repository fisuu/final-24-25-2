package edu.kit.kastel.util;

/**
 * The type invalid configuration exception.
 * This exception is thrown when the configuration is invalid.
 *
 * @author uwing
 * @author Programmieren-Team
 */
public class InvalidConfigurationException extends Exception {

    /**
     * Instantiates a new invalid configuration exception.
     *
     * @param cause the cause
     */
    public InvalidConfigurationException(String cause) {
        super(cause);
    }
}
