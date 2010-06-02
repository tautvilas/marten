package marten.aoe.engine;

public interface TerrainDatabaseListener {
    void onTerrainAdded(Terrain terrain);
    void onTerrainRemoved(Terrain terrain);
}
