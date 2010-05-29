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
    HIGHER,
    /** Marine ground units may enter this tile (overrides <code>IMPASSABLE</code> for these units).*/
    SHALLOW_WATER,
    /** Marine ground units may enter this tile (overrides <code>DIFFICULT</code> and <code>IMPASSABLE</code> for these units). */
    WATER
    // XXX: could be extended with additional features should necessity arise.
}
