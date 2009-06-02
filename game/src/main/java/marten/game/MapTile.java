package marten.game;

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
    /** A constructor with terrain initialization. 
     * @param terrain The initial terrain type of the tile.*/
    public MapTile(Terrain terrain) {
        this.originalTerrain = this.effectiveTerrain = terrain;
    }
    /** Subscribes a listener to this tile.
     * @param listener The listener to be registered.*/
    public void addListener(MapTileListener listener) {
        this.listeners.add(listener);
    }
    /** Removes a listener from the subscription list.
     * @param listener The listener to be removed.
     * @return <code>false</code> if the listener was not registered in the first place.*/
    public boolean removeListener(MapTileListener listener) {
        return this.listeners.remove(listener);
    }
    /** Changes the current terrain of the map tile and notifies the listeners if there is an actual change.
     * @param terrain The new terrain of the tile.*/
    public void changeTerrain(Terrain terrain) {
        if (terrain == this.effectiveTerrain)
            return;
        for (MapTileListener listener : this.listeners)
            listener.onTerrainChange(this, this.effectiveTerrain, terrain);
        this.effectiveTerrain = terrain;        
    }
    /** Reverts the current terrain of the map tile back to the original terrain back from the initialization of the map and notifies the listeners if there is an actual change.*/
    public void revertTerrain() {
        if (this.effectiveTerrain == this.originalTerrain)
            return;
        for (MapTileListener listener : this.listeners)
            listener.onTerrainChange(this, this.effectiveTerrain, this.originalTerrain);
        this.effectiveTerrain = this.originalTerrain;        
    }
    /** Returns the original terrain that the tile was set to during initialization.
     * @return the original type of terrain.*/
    public Terrain getOriginalTerrain() {
        return this.originalTerrain;
    }
    /** Returns the current terrain in the tile.
     * @return the current type of terrain.*/
    public Terrain getEffectiveTerrain() {
        return this.effectiveTerrain;
    }
}
