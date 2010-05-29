package marten.aoe.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public final class TileMap {
    private static HashMap<TileCoordinate, Tile> map = new HashMap<TileCoordinate, Tile>();
    private static ArrayList<TileMapListener> listeners = new ArrayList<TileMapListener>();
    private TileMap() {}
    public static void addListener(TileMapListener listener) {
        listeners.add(listener);
    }
    public static void removeListener(TileMapListener listener) {
        listeners.remove(listener);
    }
    public static void add(Tile tile) {
        if (map.containsKey(tile.at()))
            for (TileMapListener listener : listeners)
                listener.onTileRemoved(map.get(tile.at()));
        map.put(tile.at(), tile);
        for (TileMapListener listener : listeners)
            listener.onTileAdded(tile);
    }
    public static void remove(Tile tile) {
        map.remove(tile.at());
        for (TileMapListener listener : listeners)
            listener.onTileRemoved(map.get(tile.at()));
    }
    public static void removeAll() {
        for (Tile tile : map.values())
            for (TileMapListener listener : listeners)
                listener.onTileRemoved(tile);
        map.clear();
    }
    public static Tile get(TileCoordinate at) {
        return map.get(at);
    }
    public static Collection<Tile> selectAll() {
        return map.values();
    }
    public static Collection<Tile> selectRect(TileCoordinate topLeft, TileCoordinate bottomRight) {
        ArrayList<Tile> selectedTiles = new ArrayList<Tile>();
        for (Tile tile : map.values())
            if (tile.at().x() > topLeft.x() && tile.at().x() < bottomRight.x() && tile.at().y() > topLeft.y() && tile.at().y() < bottomRight.y())
                selectedTiles.add(tile);
        return selectedTiles;
    }
}
