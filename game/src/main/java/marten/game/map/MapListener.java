package marten.game.map;

public interface MapListener {
    void onTerrainChange(Map map, MapCoordinates at, Terrain oldTerrain, Terrain newTerrain);
}
