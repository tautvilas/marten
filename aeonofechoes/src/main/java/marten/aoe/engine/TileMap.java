package marten.aoe.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/** The full database of tiles currently on the map.
 * This class is a singleton and no instances of it can be created.
 * @author Petras Ražanskas */
public final class TileMap {
    private static HashMap<TileCoordinate, Tile> map = new HashMap<TileCoordinate, Tile>();
    private static ArrayList<TileMapListener> listeners = new ArrayList<TileMapListener>();
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
        if (map.containsKey(tile.at()))
            for (TileMapListener listener : listeners)
                listener.onTileRemoved(map.get(tile.at()));
        map.put(tile.at(), tile);
        for (TileMapListener listener : listeners)
            listener.onTileAdded(tile);
    }
    /** Removes a tile from the database. Listeners are notified accordingly.
     * @param the tile to be removed.*/
    public static void remove(Tile tile) {
        map.remove(tile.at());
        for (TileMapListener listener : listeners)
            listener.onTileRemoved(map.get(tile.at()));
    }
    /** Removes all tiles from the database. Listeners are notified accordingly. */
    public static void removeAll() {
        for (Tile tile : map.values())
            for (TileMapListener listener : listeners)
                listener.onTileRemoved(tile);
        map.clear();
    }
    // TODO: to make this more flexible, there should be a way to remove tiles according to some arbitrary filter.
    /** @return the tile at the given location. It is an actual tile and not its copy.
     * @param at the coordinates where the tile is located.*/
    public static Tile get(TileCoordinate at) {
        return map.get(at);
    }
    /** @return all tiles currently in database. These are actual tiles and not their copies. */
    public static Collection<Tile> selectAll() {
        return map.values();
    }
    /** @return all tiles which are within the given rectangle. These are actual tiles and not their copies.
     * @param topLeft the top-left corner of the rectangle
     * @param bottomRight the bottom-right corner of the rectangle*/
    // TODO(carnifex): to make this more flexible, some kind of general way of filtering should be defined later, and this method deprecated.
    public static Collection<Tile> selectRect(TileCoordinate topLeft, TileCoordinate bottomRight) {
        ArrayList<Tile> selectedTiles = new ArrayList<Tile>();
        for (Tile tile : map.values())
            if (tile.at().x() > topLeft.x() && tile.at().x() < bottomRight.x() && tile.at().y() > topLeft.y() && tile.at().y() < bottomRight.y())
                selectedTiles.add(tile);
        return selectedTiles;
    }
}
