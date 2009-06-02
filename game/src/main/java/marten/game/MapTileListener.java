package marten.game;

/** Allows a class to monitor changes in map tiles.
 * @author carnifex*/
public interface MapTileListener {
    /** This is invoked whenever the terrain is changed in a tile.
     * @param tile The tile where the change took place.
     * @param oldTerrain The old type of terrain.
     * @param newTerrain The new type of terrain. */
    void onTerrainChange(MapTile tile, Terrain oldTerrain, Terrain newTerrain);
}
