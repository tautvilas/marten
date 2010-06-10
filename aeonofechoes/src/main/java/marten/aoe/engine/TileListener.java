package marten.aoe.engine;

/** An interface for any class which finds it necessary to track changes on individual tiles.
 * @author Petras Ražanskas */
public interface TileListener {
    /** Method is invoked whenever terrain changes on the source tile.
     * @param source the tile where the change took place
     * @param oldValue the previous terrain of the tile */
    void onTerrainChange(Tile source, Terrain oldValue);
    /** Method is invoked whenever the name of the source tile changes.
     * @param source the tile where the change took place
     * @param oldValue the previous name of the tile*/
    void onNameChange(Tile source, String oldValue);
    /** Method is invoked whenever the access mode of a tile changes.
     * @param source the tile where the change took place
     * @param oldValue the previous access mode of the tile */
    void onAccessChange(Tile tile, boolean oldValue);    
}
