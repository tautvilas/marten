package marten.aoe.engine;

public interface TileMapListener {
    void onTileRemoved(Tile tile);
    void onTileAdded(Tile tile);
}
