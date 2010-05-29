package marten.aoe.engine;

/** An interface for any class which finds it necessary to track changes of the map.
 * It is important to know that it only reflects changes in the whole structure of the map.
 * For changes within specific tiles, consider {@linkplain TileListener}.
 * @author Petras Ražanskas */
public interface TileMapListener {
    /** Method is invoked whenever a tile is removed from the map.
     * @param tile the tile that was removed from the map.*/
    void onTileRemoved(Tile tile);
    /** Method is invoked whenever a tile is added to the map.
     * @param tile the tile that was added to the map.*/
    void onTileAdded(Tile tile);
}
