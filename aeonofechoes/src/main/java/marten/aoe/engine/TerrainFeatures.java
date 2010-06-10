package marten.aoe.engine;

/** The set of flags which describe the general behavior of any terrain
 * @author Petras Ražanskas */
public enum TerrainFeatures {
    /** Ground units should slow down */
    DIFFICULT,
    /** Ground units should not be able to enter this location. This flag overrides <code>DIFFICULT</code>. */
    IMPASSABLE,
    /** Some types of ground units should benefit defensively */
    COVERED,
    /** Some types of ground units should benefit significantly defensively. This flag overrides <code>COVERED</code>. */
    FORTIFIED,
    /** Ground units on a high ground should generally have a defensive and offensive advantage over units below. */
    HIGH,
    /** Ground units on a higher ground should have significant defensive and offensive advantage over units below and some advantage over units on a high ground. This flag overrides <code>HIGH</code>. */
    MOUNTAIN,
    /** Marine ground units may enter this tile (overrides <code>IMPASSABLE</code> for these units).*/
    SHALLOW_WATER,
    /** Marine ground units may enter this tile (overrides <code>DIFFICULT</code> and <code>IMPASSABLE</code> for these units). */
    WATER,
    /** Detrimental defense effects for units with weakness for cold.*/
    COLD,
    /** Detrimental defense effects for units with weakness for hotness.*/
    HOT,
    /** Air units cannot enter this location */
    CAVE,
    /** Detrimental defense effects for units without night-vision.*/
    DARK;
    // XXX: could be extended with additional features should necessity arise.
    public static TerrainFeatures fromString(String string) {
        if (string.equals("Difficult"))
            return DIFFICULT;
        if (string.equals("Impassable"))
            return IMPASSABLE;
        if (string.equals("Covered"))
            return COVERED;
        if (string.equals("Fortified"))
            return FORTIFIED;
        if (string.equals("High"))
            return HIGH;
        if (string.equals("Mountain"))
            return MOUNTAIN;
        if (string.equals("ShallowWater"))
            return SHALLOW_WATER;
        if (string.equals("Water"))
            return WATER;
        if (string.equals("Cold"))
            return COLD;
        if (string.equals("Hot"))
            return HOT;
        if (string.equals("Cave"))
            return CAVE;
        if (string.equals("Dark"))
            return DARK;
        return null;
    }
}
