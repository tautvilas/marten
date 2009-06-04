package marten.game;

/**This class should be everywhere a location on the map is to be referenced.
 * @author carnifex*/
public class MapCoordinates {
    /**The x coordinate (from bottom up)*/
    public int x;
    /**The y coordinate (from left to right)*/
    public int y;
    /**A constructor with initial coordinate values.
     * @param x the initial x coordinate
     * @param y the initial y coordinate*/
    public MapCoordinates (int x, int y) {
        this.x = x;
        this.y = y;
    }
    /**This method evaluates the coordinates of a location, adjacent to these coordinates in the given direction.
     * @param direction the direction to which the adjacent location is found
     * @return the coordinates of the adjacent location*/
    public MapCoordinates adjacent (MapDirection direction) {
        int newX = this.x;
        int newY = this.y;
        switch (direction) {
        case NORTHWEST:
        case SOUTHWEST:
            --newX; break;
        case NORTHEAST:
        case SOUTHEAST:
            ++newX; break;
        }
        switch (direction) {
        case NORTH:
            ++newY; break;
        case NORTHWEST:
        case NORTHEAST:
            newY += (this.x + 1) % 2; break;
        case SOUTHWEST:
        case SOUTHEAST:
            newY -= this.x % 2; break;
        case SOUTH:
            --newY; break;
        }
        return new MapCoordinates(newX, newY);
    }
}
