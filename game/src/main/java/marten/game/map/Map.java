package marten.game.map;

import java.util.ArrayList;

/**Defines all necessary data of the map and manipulations upon it.
 * @author carnifex*/
public class Map implements MapTileListener {
    
    /**The height of the map*/
    private int height;
    
    /**The width of the map*/
    private int width;
    
    /**The tiles which form the map*/
    private MapTile[][] map;
    
    /** The list of listeners. */
    private ArrayList<MapListener> listeners = new ArrayList<MapListener>();
    
    /**Constructs a map of given dimensions and fills it with <code>VOID</code> terrain tiles.
     * @param height the height of the new map
     * @param width the width of the new map
     * @throws IllegalArgumentException if either height or width is less than or equal to zero*/
    public Map (int height, int width) {
        if (height <= 0 || width <= 0)
            throw new IllegalArgumentException("Illegal map dimensions");
        this.height = height;
        this.width = width;
        this.map = new MapTile[width][height];
        for (int x = 0; x < width; ++x)
            for (int y = 0; y < height; ++y) {
                this.map[x][y] = new MapTile(Terrain.VOID, new MapCoordinates(x,y));
                this.map[x][y].addListener(this);
            }
    }
    
    /** Subscribes a listener to this map.
     * @param listener The listener to be registered.
     * @throws NullPointerException if the listener is <code>null</code>*/
    public void addListener(MapListener listener) {
        if (listener == null)
            throw new NullPointerException ("Null listener.");
        this.listeners.add(listener);
    }
    
    /** Removes a listener from the subscription list.
     * @param listener The listener to be removed.*/
    public void removeListener(MapListener listener) {
        this.listeners.remove(listener);
    }
    
    /** Removes all listeners from the subscription list.*/
    public void removeAllListeners() {
        this.listeners = new ArrayList<MapListener>();
    }
    
    /**Returns a reference to the map tile in the given location.
     * @param at the coordinates of the required tile
     * @return the map tile in given location.
     * @throws IndexOutOfBoundsException if the given coordinates are outside of the map*/
    public MapTile getTile (MapCoordinates at) {
        if (at.x < 0 || at.y < 0 || at.x >= this.width || at.y >= this.height)
            throw new IndexOutOfBoundsException("Required map tile out of bounds");
        return this.map[at.x][at.y];
    }
    
    /**Fills the map with tiles of a given terrain type.
     * @param terrain the terrain type used for filling*/
    public void fill (Terrain terrain) {
        for (int x = 0; x < this.width; ++x)
            for (int y = 0; y < this.height; ++y)
                 map[x][y].changeTerrainPermanently(terrain);
    }
    
    /**Fills the specified area (boundaries included) with tiles of a given terrain type.
     * @param from the coordinates of the top left corner of the area
     * @param to the coordinates of the bottom right corner of the area
     * @param terrain the terrain type used for filling
     * @throws IndexOutOfBoundsException if any of the given coordinates are outside of the map
     * @throws IllegalArgumentException if <code>from.x > to.x</code> or <code>from.y > to.y</code>*/
    public void fillArea (MapCoordinates from, MapCoordinates to, Terrain terrain) {
        if (from.x > to.x || from.y > to.y)
            throw new IllegalArgumentException("Illegal specification of area");
        if (from.x < 0 || from.x >= this.width || from.y < 0 || from.y >= this.height || to.x < 0 || to.x >= this.width || to.y < 0 || to.y >= this.height)
            throw new IndexOutOfBoundsException("Provided map area out of bounds");
        for (int x = from.x; x <= to.x; ++x)
            for (int y = from.y; y <= to.y; ++y)
                map[x][y].changeTerrainPermanently(terrain);                
    }
    
    /**Listens to the terrain changes of the underlying tiles and forwards these messages to <code>MapListener</code>s.
     * @param tile the originating tile of the event
     * @param oldTerrain the old terrain type in the tile
     * @param newTerrain the new terrain type in the tile*/
    @Override public void onTerrainChange(MapTile tile, Terrain oldTerrain, Terrain newTerrain) {
        if (oldTerrain != newTerrain)
            for (MapListener listener : this.listeners)
                listener.onTerrainChange(this, tile.getCoordinates(), oldTerrain, newTerrain);
    }
}
