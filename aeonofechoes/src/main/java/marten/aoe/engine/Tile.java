package marten.aoe.engine;

import java.util.ArrayList;

public final class Tile {
    private Terrain terrain;
    private TileCoordinate at;
    private String name;
    private ArrayList<TileListener> listeners = new ArrayList<TileListener>();
    public Tile(Terrain terrain, TileCoordinate at, String name) {
        this.terrain = terrain;
        this.at = at;
        this.name = name;
        TileMap.add(this);
    }
    public void addListener(TileListener listener) {
        this.listeners.add(listener);
    }
    public void removeListener(TileListener listener) {
        this.listeners.remove(listener);
    }
    public Terrain terrain() {
        return this.terrain;
    }
    public TileCoordinate at() {
        return this.at;
    }
    public String name() {
        return this.name;
    }
    public void changeTerrain(Terrain terrain) {
        Terrain oldValue = this.terrain;
        this.terrain = terrain;
        for (TileListener listener : this.listeners)
            listener.onTerrainChange(this, oldValue);
    }
    public void changeName(String name) {
        String oldValue = this.name;
        this.name = name;
        for (TileListener listener : this.listeners)
            listener.onNameChange(this, oldValue);
    }
}
