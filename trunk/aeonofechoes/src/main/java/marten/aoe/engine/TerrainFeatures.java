package marten.aoe.engine;

/** The set of flags which describe the general behavior of any terrain
 * @author Petras Ražanskas */
public enum TerrainFeatures {
    /** Ground units should slow down */
    HARD_TO_WALK,
    /** Ground units should not be able to enter this location.*/
    UNWALKABLE,
    /** Air units should slow down */
    HARD_TO_FLY,
    /** Air units should not be able to enter this location.*/
    UNFLYABLE,
    /** Marine units should slow down */
    HARD_TO_SWIM,
    /** Marine units should not be able to enter this location.*/
    UNSWIMMABLE,
    /** Some types of ground units should benefit defensively */
    COVERED,
    /** Some types of ground units should benefit significantly defensively.*/
    FORTIFIED,
    /** Ground units on a high ground should generally have a defensive and offensive advantage over units below. */
    HIGH,
    /** Ground units on a higher ground should have significant defensive and offensive advantage over units below and some advantage over units on a high ground. This flag overrides <code>HIGH</code>. */
    VERY_HIGH,
    /** Detrimental defense effects for units with weakness for cold.*/
    COLD,
    /** Detrimental defense effects for units with weakness for hotness.*/
    HOT,
    /** Detrimental defense effects for night-dwellers.*/
    LIT,
    /** Detrimental defense effects for units without night-vision.*/
    DARK,
    /** This location is a buildable part of a base.*/
    CITY,
    /** This location is a central part of a base*/
    BASE,
    /** This location is a strategic point of a map*/
    VILLAGE,
    /** This location could be easier to move through for ground units*/
    ROAD;
    // XXX: could be extended with additional features should necessity arise.
    public static TerrainFeatures fromString(String string) {
        if (string.equals("HardToWalk"))
            return HARD_TO_WALK;
        if (string.equals("Unwalkable"))
            return UNWALKABLE;
        if (string.equals("HardToSwim"))
            return HARD_TO_SWIM;
        if (string.equals("Unswimmable"))
            return UNSWIMMABLE;
        if (string.equals("HardToFly"))
            return HARD_TO_FLY;
        if (string.equals("Unflyable"))
            return UNFLYABLE;
        if (string.equals("Covered"))
            return COVERED;
        if (string.equals("Fortified"))
            return FORTIFIED;
        if (string.equals("High"))
            return HIGH;
        if (string.equals("VeryHigh"))
            return VERY_HIGH;
        if (string.equals("Cold"))
            return COLD;
        if (string.equals("Hot"))
            return HOT;
        if (string.equals("Dark"))
            return DARK;
        if (string.equals("Lit"))
            return LIT;
        if (string.equals("City"))
            return CITY;
        if (string.equals("Base"))
            return BASE;
        if (string.equals("Village"))
            return VILLAGE;
        if (string.equals("Road"))
            return ROAD;
        return null;
    }
}
