package marten.aoe.depreciated.engine;

/*     1,2     3,2
 * 0,1  |  2,1  |  4,1
 *  |  1,1  |  3,1  |
 * 0,0  |  2,0  |  4,0
 *  |  1,0  |  3,0  |
 */

/** Defines an immutable coordinate on a hexagonal grid.
 * X axis is left-to-right, Y axis is bottom-to-top 
 * @author Petras Ražanskas */
public final class TileCoordinate {
    private int x, y;
    /** Creates a point on the map with defined coordinates
     * @param x the horizontal coordinate (from the left to the right)
     * @param y the vertical coordinate (from the bottom to the top) */
    public TileCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
    /** @return the x coordinate of the given point of a map */
    public int x() {
        return this.x;
    }
    /** @return the y coordinate of the given point of a map */
    public int y() {
        return this.y;
    }
    /** Calculates the coordinates of an adjacent tile on a hexagonal grid. 
     * @return the coordinates of a tile, adjacent to this one and located in a given direction.
     * @param direction the direction to which the next coordinate lies. */
    public TileCoordinate adjacent(TileDirection direction) {
        int newX = this.x + direction.deltaX();
        int newY = this.y + (this.y % 2 == 1 ? direction.deltaYodd() : direction.deltaYeven());
        return new TileCoordinate(newX, newY);
    }
    /** @param other The other set of coordinate this is compared to. 
     * @return <code>true</code> if and only if both sets of coordinates match exactly */
    public boolean equals(Object other) {
        if (other == null || this.hashCode() != other.hashCode() || !(other instanceof TileCoordinate))
            return false;
        TileCoordinate otherCoordinate = (TileCoordinate)other;
        return this.x == otherCoordinate.x && this.y == otherCoordinate.y;
    }
    /** @return an identifier which quasi-uniquely describes the contents of the object for hashing purposes */
    public int hashCode() {
        return (this.x << 8) ^ this.y;
    }
}
