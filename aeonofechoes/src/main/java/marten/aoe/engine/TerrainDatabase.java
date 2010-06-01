package marten.aoe.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** The database to store all configurations of terrain in the game.
 * This database does not allow its entries to be removed and attempts at duplicate entries will cause immediate termination.
 * @author Petras Ražanskas */
public final class TerrainDatabase {
    private static Map<String, Terrain> database = new HashMap<String, Terrain>();
    private static List<TerrainDatabaseListener> listeners = new ArrayList<TerrainDatabaseListener>();
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
        for (TerrainDatabaseListener listener : listeners)
            listener.onTerrainAdded(terrain);
    }
    /** Adds a new listener to track information on database changes 
     * @param listener the new listener to be connected to the database.*/
    public static void addListener(TerrainDatabaseListener listener) {
        listeners.add(listener);
    }
    /** Removes a listener which no longer wishes to track database changes 
     * @param listener the listener to be disconnected from the database.*/
    public static void removeListener(TerrainDatabaseListener listener) {
        listeners.remove(listener);
    }
    /** @return the complete definition of terrain with the given name */
    public static Terrain get(String name) {
        return database.get(name);
    }
    /** @return the complete list of currently defined terrain types */
    public static Set<String> definedTerrain() {
        return database.keySet();
    }
}
