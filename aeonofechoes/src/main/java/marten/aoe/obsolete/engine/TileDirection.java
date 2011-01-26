package marten.aoe.obsolete.engine;

/** Defines all six possible directions on a hexagonal grid 
 * @author Petras Ražanskas*/
public enum TileDirection {
    /** Direction towards the top of a grid (higher Y) */
    NORTH(0, 1, 1),
    /** Direction towards the top-right of a grid (higher Y, higher X) */
    NORTHEAST(1, 0, 1),
    /** Direction towards the bottom-right of a grid (lower Y, higher X) */
    SOUTHEAST(1, -1, 0),
    /** Direction towards the bottom of a grid (lower Y) */
    SOUTH(0, -1, -1),
    /** Direction towards the bottom-left of a grid (lower Y, lower X) */
    SOUTHWEST(-1, -1, 0),
    /** Direction towards the top-left of a grid (higher Y, lower X) */
    NORTHWEST(-1, 0, 1);
    private int deltaX;
    private int deltaYodd;
    private int deltaYeven;
    private TileDirection(int deltaX, int deltaYodd, int deltaYeven) {
        this.deltaX = deltaX;
        this.deltaYodd = deltaYodd;
        this.deltaYeven = deltaYeven;
    }
    public int deltaX() {
        return this.deltaX;
    }
    public int deltaYodd() {
        return this.deltaYodd;
    }
    public int deltaYeven() {
        return this.deltaYeven;
    }
}
