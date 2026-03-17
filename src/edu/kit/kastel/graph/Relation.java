package edu.kit.kastel.graph;

/**
 * The enum relation.
 * This enum represents the relations between two items.
 *
 * @author uwing
 */
public enum Relation {

    /**
     * Contains relation.
     * Relation between a category and a product.
     */
    CONTAINS_RELATION("contains"),
    /**
     * Contained-in relation.
     * Relation between a product and a category.
     */
    CONTAINED_IN_RELATION("contained-in"),
    /**
     * Part-of relation.
     * Relation between two products.
     */
    PART_OF_RELATION("part-of"),
    /**
     * Has-part relation.
     * Relation between two products.
     */
    HAS_PART_RELATION("has-part"),
    /**
     * Successor-of relation.
     * Relation between two products.
     */
    SUCCESSOR_OF_RELATION("successor-of"),
    /**
     * Predecessor-of relation.
     * Relation between two products.
     */
    PREDECESSOR_OF_RELATION("predecessor-of");

    private final String name;

    Relation(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the relation.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the relation by name.
     *
     * @param name the name
     *
     * @return the relation if it exists, otherwise null
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
     * Method to get a reversal relation.
     *
     * @return the reversal relation
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
     * Checks if a relation between two items is valid.
     *
     * @param source    the source item
     * @param neighbour the neighbour item
     *
     * @return true if the relation is valid, false otherwise
     */
    public boolean validRelation(Item source, Item neighbour) {
        return switch (this) {
            case CONTAINS_RELATION -> source.getType().equals(ItemType.CATEGORY) && neighbour != null;
            case CONTAINED_IN_RELATION -> source != null && neighbour.getType().equals(ItemType.CATEGORY);
            case PART_OF_RELATION, HAS_PART_RELATION,
                 SUCCESSOR_OF_RELATION, PREDECESSOR_OF_RELATION ->
                    source.getType().equals(ItemType.PRODUCT) && neighbour.getType().equals(ItemType.PRODUCT);
        };

    }
}
