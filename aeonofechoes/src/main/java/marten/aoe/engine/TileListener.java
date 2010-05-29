package marten.aoe.engine;

public interface TileListener {
    void onTerrainChange(Tile source, Terrain oldValue);
    void onNameChange(Tile source, String oldValue);    
}
