package edu.kit.kastel;

import edu.kit.kastel.commands.InvalidCommandArgumentException;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Clean up arguments.
 *
 * @author uwing
 */
public final class CleanUpArguments {

    private CleanUpArguments() {
    }

    /**
     * Clean term list.
     *
     * @param commandArgument the command argument
     * @return the list
     * @throws InvalidCommandArgumentException the invalid command argument exception
     */
    public static List<String> cleanTerm(String commandArgument) throws InvalidCommandArgumentException {

        List<String> tokens = new ArrayList<>(tokenize(cleanWhitespace(commandArgument)));
        tokens.removeIf(t -> t.equals(","));
        return tokens;
    }

    private static List<String> tokenize(String input) {
        return List.of(input.split("(?=[(),])|(?<=[(),])|\\\\b(UNION|INTERSECTION)\\\\b"));
    }

    /**
     * Clean whitespace string.
     *
     * @param input the input
     * @return the string
     */
    public static String cleanWhitespace(String input) {
        if (input == null) {
            return null;
        }

        String result = input;

        result = result.replaceAll("\\s+", " ");

        result = result.replaceAll("\\(\\s+", "(");

        result = result.replaceAll("\\s+\\(", "(");

        result = result.replaceAll("\\)\\s+", ")");

        result = result.replaceAll("\\s+\\)", ")");

        result = result.replaceAll("\\s+,", ",");

        result = result.replaceAll(",\\s+", ",");

        return result.trim();
    }

    /**
     * Clean argument if bracket is part of word.
     *
     * @param line the line
     * @param i    the
     * @param c    the c
     */
    public static void cleanArgumentIfBracketIsPartOfWord(List<String> line, int i, char c) {

    }
}
