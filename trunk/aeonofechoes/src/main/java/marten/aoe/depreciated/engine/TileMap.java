package marten.aoe.depreciated.engine;

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
    private Map<TileCoordinate, Tile> map = new HashMap<TileCoordinate, Tile>();
    private List<TileMapListener> listeners = new ArrayList<TileMapListener>();
    private String name = "";
    public TileMap() {}
    /** Adds a listener to the database, which will track addition and removal of tiles from it.
     * @param listener the listener to track changes in the database */
    public void addListener(TileMapListener listener) {
        this.listeners.add(listener);
    }
    /** Removes the listener from the database so it no longer tracks its changes
     * @param listener the listener to be removed.*/
    public void removeListener(TileMapListener listener) {
        this.listeners.remove(listener);
    }
    /** Adds a new tile to the database.
     * If there is an existing tile with the same coordinates, it is implicitly removed.
     * This method notifies the listeners both of the removal and addition of the tiles involved. 
     * @param tile the new tile to be added into the database. */
    public void add(Tile tile) {
        if (this.map.containsValue(tile))
            return;
        if (this.map.containsKey(tile.at()))
            for (TileMapListener listener : this.listeners)
                listener.onTileRemoved(this.map.get(tile.at()));
        this.map.put(tile.at(), tile);
        for (TileMapListener listener : this.listeners)
            listener.onTileAdded(tile);
    }
    /** Removes a tile from the database. Listeners are notified accordingly.
     * @param the coordinates of the tile to be removed.*/
    public void remove(TileCoordinate tile) {
        if (!this.map.containsKey(tile))
            throw new IndexOutOfBoundsException("The tile does not exist at "+tile.x()+", "+tile.y());
        Tile removedTile = this.map.remove(tile);
        for (TileMapListener listener : this.listeners)
            listener.onTileRemoved(removedTile);
    }
    /** Removes all tiles from the database. Listeners are notified accordingly. */
    public void removeAll() {
        for (Tile tile : this.map.values())
            for (TileMapListener listener : this.listeners)
                listener.onTileRemoved(tile);
        this.map.clear();
        this.name = "";
    }
    // TODO: to make this more flexible, there should be a way to remove tiles according to some arbitrary filter.
    /** @return the tile at the given location. It is an actual tile and not its copy. <code>null</code> is returned if there is no such tile.
     * @param at the coordinates where the tile is located.*/
    public Tile get(TileCoordinate tile) {
        if (!this.map.containsKey(tile))
            throw new IndexOutOfBoundsException("The tile does not exist at "+tile.x()+", "+tile.y());
        return this.map.get(tile);
    }
    /** @return all tiles currently in database. These are actual tiles and not their copies. */
    public Collection<Tile> selectAll() {
        return this.map.values();
    }
    /** @return all tiles which are within the given rectangle. These are actual tiles and not their copies.
     * @param topLeft the top-left corner of the rectangle
     * @param bottomRight the bottom-right corner of the rectangle*/
    // TODO: to make this more flexible, some kind of general way of filtering should be defined later, and this method deprecated.
    public Collection<Tile> selectRect(TileCoordinate topLeft, TileCoordinate bottomRight) {
        ArrayList<Tile> selectedTiles = new ArrayList<Tile>();
        for (Tile tile : this.map.values())
            if (tile.at().x() > topLeft.x() && tile.at().x() < bottomRight.x() && tile.at().y() > topLeft.y() && tile.at().y() < bottomRight.y())
                selectedTiles.add(tile);
        return selectedTiles;
    }
    /** @return the set of all coordinates that are used in this map*/
    public Set<TileCoordinate> definedCoordinates() {
        return this.map.keySet();
    }
    /** @return <code>true</code> if there are no tiles defined in the map.*/
    public boolean isEmpty() {
        return this.map.isEmpty();
    }
    /** @return the name of the currently loaded tile map.*/
    public String name() {
        return this.name;
    }
    /** Sets the name for the currently loaded map.*/
    public void name(String newName) {
        this.name = newName;
    }
    /** @return the minimum x coordinate in this map.*/
    public int minX() {
        if (this.map.isEmpty())
            throw new IllegalStateException("Map is empty, requested value is undefined");
        int minX = Integer.MAX_VALUE;
        for (TileCoordinate coordinate : this.map.keySet())
            if (minX > coordinate.x())
                minX = coordinate.x();
        return minX;
    }
    /** @return the maximum x coordinate in this map.*/
    public int maxX() {
        if (this.map.isEmpty())
            throw new IllegalStateException("Map is empty, requested value is undefined");
        int maxX = Integer.MIN_VALUE;
        for (TileCoordinate coordinate : this.map.keySet())
            if (maxX < coordinate.x())
                maxX = coordinate.x();
        return maxX;
    }
    /** @return the minimum y coordinate in this map.*/
    public int minY() {
        if (this.map.isEmpty())
            throw new IllegalStateException("Map is empty, requested value is undefined");
        int minY = Integer.MAX_VALUE;
        for (TileCoordinate coordinate : this.map.keySet())
            if (minY > coordinate.y())
                minY = coordinate.y();
        return minY;
    }
    /** @return the maximum y coordinate in this map.*/
    public int maxY() {
        if (this.map.isEmpty())
            throw new IllegalStateException("Map is empty, requested value is undefined");
        int maxY = Integer.MIN_VALUE;
        for (TileCoordinate coordinate : this.map.keySet())
            if (maxY < coordinate.y())
                maxY = coordinate.y();
        return maxY;
    }
}
