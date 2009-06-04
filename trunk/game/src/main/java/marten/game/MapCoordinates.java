package marten.game;

public class MapCoordinates {
    public int x;
    public int y;
    public MapCoordinates (int x, int y) {
        this.x = x;
        this.y = y;
    }
    public MapCoordinates adjacent (MapDirection direction) {
        switch (direction) {
        case NORTH:
            return new MapCoordinates(x, y + 1);
        case SOUTH:
            return new MapCoordinates(x, y - 1);
        }
        if (x % 2 == 0)
            switch (direction) {
            case NORTHWEST:
                return new MapCoordinates(x - 1, y + 1);
            case NORTHEAST:
                return new MapCoordinates(x + 1, y + 1);
            case SOUTHWEST:
                return new MapCoordinates(x - 1, y);
            case SOUTHEAST:
                return new MapCoordinates(x + 1, y);
            }                
        else
            switch (direction) {
            case NORTHWEST:
                return new MapCoordinates(x - 1, y);
            case NORTHEAST:
                return new MapCoordinates(x + 1, y);
            case SOUTHWEST:
                return new MapCoordinates(x - 1, y - 1);
            case SOUTHEAST:
                return new MapCoordinates(x + 1, y - 1);
            }
        /* This should never be reached */
        throw new RuntimeException ("Behold a miracle. Someone messed with MapDirection class apparently.");
    }
}
