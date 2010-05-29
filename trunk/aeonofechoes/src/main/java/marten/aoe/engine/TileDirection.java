package marten.aoe.engine;

/** Defines all six possible directions on a hexagonal grid 
 * @author Petras Ražanskas*/
public enum TileDirection {
    /** Direction towards the top of a grid (lower Y) */
    NORTH,
    /** Direction towards the top-right of a grid (lower Y, higher X) */
    NORTHEAST,
    /** Direction towards the bottom-right of a grid (higher Y, higher X) */
    SOUTHEAST,
    /** Direction towards the bottom of a grid (higher Y) */
    SOUTH,
    /** Direction towards the bottom-left of a grid (higher Y, lower X) */
    SOUTHWEST,
    /** Direction towards the top-left of a grid (lower Y, lower X) */
    NORTHWEST
}
