package marten.aoe.engine;

import java.util.HashMap;
import java.util.Map;

public final class TerrainDatabase {
    private static Map<String, Terrain> database = new HashMap<String, Terrain>();
    private TerrainDatabase() {} // Prevent TileDatabase objects from being created
    public static void add(Terrain tile) {
        if (database.containsKey(tile.name()))
            throw new IllegalArgumentException("Duplicate terrain names in the database");
        database.put(tile.name(), tile);
    }
    public static void fetch(String name) {
        database.get(name);
    }
}
