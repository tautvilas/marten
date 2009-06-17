package marten.game.map;

import java.util.ArrayList;

/** Defines all necessary data to describe a map tile and manipulations upon it.
 * @author carnifex*/
public class MapTile {
    
    /** The initial type of terrain. */
    private Terrain originalTerrain;
    
    /** The current type of terrain. */
    private Terrain effectiveTerrain;
    
    /** The list of listeners. */
    private ArrayList<MapTileListener> listeners = new ArrayList<MapTileListener>();
    
    /** The coordinates of the tile in the map.*/
    private MapCoordinates coordinates;
    
    /** A constructor with terrain initialization and coordinate assignment. 
     * @param terrain The initial terrain type of the tile.
     * @param coordinates The coordinates of the tile in the map*/
    public MapTile(Terrain terrain, MapCoordinates coordinates) {
        this.originalTerrain = this.effectiveTerrain = terrain;
        this.coordinates = coordinates;
    }
    
    /** Subscribes a listener to this tile.
     * @param listener The listener to be registered.
     * @throws NullPointerException if the listener is <code>null</code>*/
    public void addListener(MapTileListener listener) {
        if (listener == null)
            throw new NullPointerException ("Null listener.");
        this.listeners.add(listener);
    }
    
    /** Removes a listener from the subscription list.
     * @param listener The listener to be removed.*/
    public void removeListener(MapTileListener listener) {
        this.listeners.remove(listener);
    }
    
    /** Removes all listeners from the subscription list.*/
    public void removeAllListeners() {
        this.listeners = new ArrayList<MapTileListener>();
    }
    
    /** Changes the current terrain of the map tile temporarily.
     * Fails silently if the terrain is already of that type.
     * Otherwise, notifies the listeners via <code>onTerrainChange</code> event.
     * @param terrain The new terrain of the tile.*/
    public void changeTerrainTemporarily(Terrain terrain) {
        if (terrain == this.effectiveTerrain)
            return;
        for (MapTileListener listener : this.listeners)
            listener.onTerrainChange(this, this.effectiveTerrain, terrain);
        this.effectiveTerrain = terrain;        
    }
    
    /** Reverts the current terrain of the map tile back to the original terrain.
     * Fails silently if the terrain is already the same as original.
     * Otherwise, notifies listeners via <code>onTerrainChange</code> event.*/
    public void revertTerrain() {
        if (this.effectiveTerrain == this.originalTerrain)
            return;
        for (MapTileListener listener : this.listeners)
            listener.onTerrainChange(this, this.effectiveTerrain, this.originalTerrain);
        this.effectiveTerrain = this.originalTerrain;        
    }
    
    /** Changes the current terrain of the map tile permanently.
     * Fails silently if the terrain is already of that type.
     * Otherwise, notifies the listeners via <code>onTerrainChange</code> event.
     * @param terrain The new terrain of the tile.*/
    public void changeTerrainPermanently(Terrain terrain) {
        this.originalTerrain = terrain;
        if (terrain == this.effectiveTerrain)
            return;
        for (MapTileListener listener : this.listeners)
            listener.onTerrainChange(this, this.effectiveTerrain, terrain);
        this.effectiveTerrain = terrain;        
    }
    
    /** Returns the original terrain of the tile.
     * @return the original type of terrain.*/
    public Terrain getOriginalTerrain() {
        return this.originalTerrain;
    }
    
    /** Returns the current terrain in the tile.
     * @return the current type of terrain.*/
    public Terrain getEffectiveTerrain() {
        return this.effectiveTerrain;
    }
    
   /** Returns the coordinates of the tile.*/
    public MapCoordinates getCoordinates() {
        return this.coordinates;
    }
}
