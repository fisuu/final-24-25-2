package edu.kit.kastel.util;

import edu.kit.kastel.graph.ItemType;
import java.util.List;

/**
 * The record item parameters.
 * This class is used to store the parameters of an item.
 * The parameters are the source name, the source id, the neighbour name, the neighbour id, the subject type and the object type.
 *
 * @param sourceName    the source name
 * @param sourceId      the source id
 * @param neighbourName the neighbour name
 * @param neighbourId   the neighbour id
 * @param subjectType   the subject type
 * @param objectType    the object type
 * @author uwing
 */
public record ItemParameters(
        String sourceName,
        int sourceId,
        String neighbourName,
        int neighbourId,
        ItemType subjectType,
        ItemType objectType
) {

    private static final String INVALID_SYNTAX_MESSAGE = "invalid syntax. Use <item> <relation> <item>'";
    private static final int SUBJECT_NAME_LINE_INDEX = 0;
    private static final int MIN_ARG_LENGTH = 3;
    private static final int OBJECT_NAME_INDEX = 4;
    private static final int OBJECT_ID_LINE_INDEX = 2;
    private static final int SUBJECT_ID_LINE_INDEX = 2;
    private static final int ID_SUBSTRING_BEGIN_INDEX = 3;
    private static final int CATEGORY_ID = -1;
    private static final int SUBJECT_HAS_ID_OPENING_BRACKET_LINE_INDEX = 1;

    /**
     * Gets the item parameters.
     *
     * @param line the line to parse
     * @return the item parameters as a record. (sourceName, sourceId, neighbourName, neighbourId, subjectType, objectType)
     * @throws InvalidConfigurationException the invalid config exception
     */
    public static ItemParameters getItemParameters(List<String> line) throws InvalidConfigurationException {
        if (line.size() < MIN_ARG_LENGTH) {
            throw new InvalidConfigurationException(INVALID_SYNTAX_MESSAGE);
        }
        String sourceName = line.get(SUBJECT_NAME_LINE_INDEX).toLowerCase();
        int sourceId;
        ItemType subjectType;
        if (line.get(SUBJECT_HAS_ID_OPENING_BRACKET_LINE_INDEX).equals(RecursiveDescentParser.OPENING_BRACKET)) {
            try {
                sourceId = Integer.parseInt(line.get(SUBJECT_ID_LINE_INDEX).substring(ID_SUBSTRING_BEGIN_INDEX));
            } catch (NumberFormatException e) {
                throw new InvalidConfigurationException(FileReader.INVALID_SERIAL_NUMBER_MESSAGE);
            }
            subjectType = ItemType.PRODUCT;

        } else {
            sourceId = CATEGORY_ID;
            subjectType = ItemType.CATEGORY;
        }
        String neighbourName;
        int neighbourId;
        ItemType objectType;
        if (line.get(line.size() - FileReader.INDEX_OFFSET).equals(RecursiveDescentParser.CLOSING_BRACKET)) {
            neighbourName = line.get(line.size() - OBJECT_NAME_INDEX).toLowerCase();
            try {
                neighbourId = Integer.parseInt(line.get(line.size() - OBJECT_ID_LINE_INDEX).substring(ID_SUBSTRING_BEGIN_INDEX));
            } catch (NumberFormatException e) {
                throw new InvalidConfigurationException(FileReader.INVALID_SERIAL_NUMBER_MESSAGE);
            }
            objectType = ItemType.PRODUCT;
        } else {
            neighbourName = line.get(line.size() - FileReader.INDEX_OFFSET).toLowerCase();
            neighbourId = CATEGORY_ID;
            objectType = ItemType.CATEGORY;
        }
        return new ItemParameters(sourceName, sourceId, neighbourName, neighbourId, subjectType, objectType);
    }
}
