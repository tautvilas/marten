package marten.aoe.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** The full database of tiles currently on the map.
 * This class is a singleton and no instances of it can be created.
 * @author Petras Ražanskas */
public final class TileMap {
    private static Map<TileCoordinate, Tile> map = new HashMap<TileCoordinate, Tile>();
    private static List<TileMapListener> listeners = new ArrayList<TileMapListener>();
    private static String name = "";
    private TileMap() {}
    /** Adds a listener to the database, which will track addition and removal of tiles from it.
     * @param listener the listener to track changes in the database */
    public static void addListener(TileMapListener listener) {
        listeners.add(listener);
    }
    /** Removes the listener from the database so it no longer tracks its changes
     * @param listener the listener to be removed.*/
    public static void removeListener(TileMapListener listener) {
        listeners.remove(listener);
    }
    /** Adds a new tile to the database.
     * If there is an existing tile with the same coordinates, it is implicitly removed.
     * This method notifies the listeners both of the removal and addition of the tiles involved. 
     * @param tile the new tile to be added into the database. */
    public static void add(Tile tile) {
        if (map.containsValue(tile))
            return;
        if (map.containsKey(tile.at()))
            for (TileMapListener listener : listeners)
                listener.onTileRemoved(map.get(tile.at()));
        map.put(tile.at(), tile);
        for (TileMapListener listener : listeners)
            listener.onTileAdded(tile);
    }
    /** Removes a tile from the database. Listeners are notified accordingly.
     * @param the coordinates of the tile to be removed.*/
    public static void remove(TileCoordinate tile) {
        if (!map.containsKey(tile))
            throw new IndexOutOfBoundsException("The tile does not exist at "+tile.x()+", "+tile.y());
        Tile removedTile = map.remove(tile);
        for (TileMapListener listener : listeners)
            listener.onTileRemoved(removedTile);
    }
    /** Removes all tiles from the database. Listeners are notified accordingly. */
    public static void removeAll() {
        for (Tile tile : map.values())
            for (TileMapListener listener : listeners)
                listener.onTileRemoved(tile);
        map.clear();
        name = "";
    }
    // TODO: to make this more flexible, there should be a way to remove tiles according to some arbitrary filter.
    /** @return the tile at the given location. It is an actual tile and not its copy. <code>null</code> is returned if there is no such tile.
     * @param at the coordinates where the tile is located.*/
    public static Tile get(TileCoordinate tile) {
        if (!map.containsKey(tile))
            throw new IndexOutOfBoundsException("The tile does not exist at "+tile.x()+", "+tile.y());
        return map.get(tile);
    }
    /** @return all tiles currently in database. These are actual tiles and not their copies. */
    public static Collection<Tile> selectAll() {
        return map.values();
    }
    /** @return all tiles which are within the given rectangle. These are actual tiles and not their copies.
     * @param topLeft the top-left corner of the rectangle
     * @param bottomRight the bottom-right corner of the rectangle*/
    // TODO: to make this more flexible, some kind of general way of filtering should be defined later, and this method deprecated.
    public static Collection<Tile> selectRect(TileCoordinate topLeft, TileCoordinate bottomRight) {
        ArrayList<Tile> selectedTiles = new ArrayList<Tile>();
        for (Tile tile : map.values())
            if (tile.at().x() > topLeft.x() && tile.at().x() < bottomRight.x() && tile.at().y() > topLeft.y() && tile.at().y() < bottomRight.y())
                selectedTiles.add(tile);
        return selectedTiles;
    }
    /** @return the set of all coordinates that are used in this map*/
    public static Set<TileCoordinate> definedCoordinates() {
        return map.keySet();
    }
    /** @return <code>true</code> if there are no tiles defined in the map.*/
    public static boolean isEmpty() {
        return map.isEmpty();
    }
    /** @return the name of the currently loaded tile map.*/
    public static String name() {
        return name;
    }
    /** Sets the name for the currently loaded map.*/
    public static void name(String newName) {
        name = newName;
    }
    /** @return the minimum x coordinate in this map.*/
    public static int minX() {
        if (map.isEmpty())
            throw new IllegalStateException("Map is empty, requested value is undefined");
        int minX = Integer.MAX_VALUE;
        for (TileCoordinate coordinate : map.keySet())
            if (minX > coordinate.x())
                minX = coordinate.x();
        return minX;
    }
    /** @return the maximum x coordinate in this map.*/
    public static int maxX() {
        if (map.isEmpty())
            throw new IllegalStateException("Map is empty, requested value is undefined");
        int maxX = Integer.MIN_VALUE;
        for (TileCoordinate coordinate : map.keySet())
            if (maxX < coordinate.x())
                maxX = coordinate.x();
        return maxX;
    }
    /** @return the minimum y coordinate in this map.*/
    public static int minY() {
        if (map.isEmpty())
            throw new IllegalStateException("Map is empty, requested value is undefined");
        int minY = Integer.MAX_VALUE;
        for (TileCoordinate coordinate : map.keySet())
            if (minY > coordinate.y())
                minY = coordinate.y();
        return minY;
    }
    /** @return the maximum y coordinate in this map.*/
    public static int maxY() {
        if (map.isEmpty())
            throw new IllegalStateException("Map is empty, requested value is undefined");
        int maxY = Integer.MIN_VALUE;
        for (TileCoordinate coordinate : map.keySet())
            if (maxY < coordinate.y())
                maxY = coordinate.y();
        return maxY;
    }
}
