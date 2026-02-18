package edu.kit.kastel;

/**
 * The enum Relation.
 *
 * @author uwing
 */
public enum Relation {

    /**
     * Contains relation relation.
     */
    CONTAINS_RELATION("contains"),
    /**
     * Contained in relation relation.
     */
    CONTAINED_IN_RELATION("contained-in"),
    /**
     * Part of relation relation.
     */
    PART_OF_RELATION("part-of"),
    /**
     * Has part relation relation.
     */
    HAS_PART_RELATION("has-part"),
    /**
     * Successor of relation relation.
     */
    SUCCESSOR_OF_RELATION("successor-of"),
    /**
     * Predecessor of relation relation.
     */
    PREDECESSOR_OF_RELATION("predecessor-of");

    private final String name;

    Relation(String name) {
        this.name = name;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets relation.
     *
     * @param name the name
     * @return the relation
     */
    public static Relation getRelation(String name) {
        for (Relation relation : values()) {
            if (relation.getName().equals(name)) {
                return relation;
            }
        }
        return null;
    }

    /**
     * Reversal relation relation.
     *
     * @return the relation
     */
    public Relation reversalRelation() {
        return switch (this) {
            case CONTAINS_RELATION -> CONTAINED_IN_RELATION;
            case CONTAINED_IN_RELATION -> CONTAINS_RELATION;
            case PART_OF_RELATION -> HAS_PART_RELATION;
            case HAS_PART_RELATION -> PART_OF_RELATION;
            case SUCCESSOR_OF_RELATION -> PREDECESSOR_OF_RELATION;
            case PREDECESSOR_OF_RELATION -> SUCCESSOR_OF_RELATION;
        };
    }

    /**
     * Valid relation boolean.
     *
     * @param source    the source
     * @param neighbour the neighbour
     * @return the boolean
     */
    public boolean validRelation(Item source, Item neighbour) {
        return switch (this) {
            case CONTAINS_RELATION -> source.getId() == -1 && neighbour != null;
            case CONTAINED_IN_RELATION -> source != null && neighbour.getId() == -1;
            case PART_OF_RELATION, HAS_PART_RELATION,
                 SUCCESSOR_OF_RELATION, PREDECESSOR_OF_RELATION -> source.getId() != -1 && neighbour.getId() != -1;
        };

    }
}
