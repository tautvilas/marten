package marten.aoe.engine;

import java.util.HashMap;
import java.util.Map;

/** The database to store all configurations of terrain in the game.
 * This database does not allow its entries to be removed and attempts at duplicate entries will cause immediate termination.
 * @author Petras Ražanskas */
public final class TerrainDatabase {
    private static Map<String, Terrain> database = new HashMap<String, Terrain>();
    private TerrainDatabase() {}
    /** Adds a new terrain entry to the database.
     * @param terrain the new type of terrain.
     * @throws IllegalArgumentException when terrain has a duplicate name but is different from another terrain in database. */
    public static void add(Terrain terrain) {
        if (database.containsValue(terrain))
            return;
        if (database.containsKey(terrain.name()))
            throw new IllegalArgumentException("Duplicate terrain names in the database");
        database.put(terrain.name(), terrain);
    }
    /** @return the complete definition of terrain with the given name */
    public static void get(String name) {
        database.get(name);
    }
}
