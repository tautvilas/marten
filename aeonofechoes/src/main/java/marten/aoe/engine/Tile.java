package marten.aoe.engine;

import java.util.ArrayList;

/** A data structure which contains all information pertaining to a single location on a map.
 * @author Petras Ražanskas */
public final class Tile {
    private Terrain terrain;
    private TileCoordinate at;
    private String name;
    private ArrayList<TileListener> listeners = new ArrayList<TileListener>();
    /** Creates a new tile with defined terrain and name at a given location and registers it on a map.
     * @param terrain The terrain at this tile
     * @param at The location of this tile on a hexagonal grid
     * @param name The name of the location*/
    public Tile(Terrain terrain, TileCoordinate at, String name) {
        this.terrain = terrain;
        this.at = at;
        this.name = name;
        TileMap.add(this);
    }
    /** Adds a listener to this tile which will track the changes in the tile
     * @param listener the listener to track the state of the tile */
    public void addListener(TileListener listener) {
        this.listeners.add(listener);
    }
    /** Removes a listener from this tile
     * @param listener the listener, which will no longer track the state of the tile */
    public void removeListener(TileListener listener) {
        this.listeners.remove(listener);
    }
    /** @return the terrain on this location */
    public Terrain terrain() {
        return this.terrain;
    }
    /** @return the coordinates of this location */
    public TileCoordinate at() {
        return this.at;
    }
    /** @return the name of this location */
    public String name() {
        return this.name;
    }
    /** Changes the current terrain of the location and informs all of the listeners about that
     * @param the new type of terrain in this location */
    public void changeTerrain(Terrain terrain) {
        Terrain oldValue = this.terrain;
        this.terrain = terrain;
        for (TileListener listener : this.listeners)
            listener.onTerrainChange(this, oldValue);
    }
    /** Changes the name of this location and informs all of the listeners about that
     * @param the new name of this location */
    public void changeName(String name) {
        String oldValue = this.name;
        this.name = name;
        for (TileListener listener : this.listeners)
            listener.onNameChange(this, oldValue);
    }
    /** @return <code>true</code> if and only if the object is an instance of tile with exactly the same terrain type, name and coordinates.
     * @param other the object this tile is being compared to.*/
    public boolean equals(Object other) {
        if (!(other instanceof Tile))
            return false;
        Tile otherTile = (Tile)other;
        return this.at.equals(otherTile.at) && this.name.equals(otherTile.name) && this.terrain.equals(otherTile.terrain);
    }
    /** @return the hashcode of this instance (the sum of the hashcodes of its publicly accessible fields).*/
    public int hashCode() {
        return this.at.hashCode() + this.name.hashCode() + this.terrain.hashCode();
    }
}
