package marten.aoe.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** The database to store all configurations of terrain currently used in the game.
 * @author Petras Ražanskas */
public final class TerrainDatabase {
    private static Map<String, Terrain> database = new HashMap<String, Terrain>();
    private static List<TerrainDatabaseListener> listeners = new ArrayList<TerrainDatabaseListener>();
    private TerrainDatabase() {}
    /** Adds a new terrain entry to the database.
     * If there is a pre-existing terrain type with the same name, it is implicitly removed unless it is equivalent to the terrain type being added.
     * @param terrain the new type of terrain. */
    public static void add(Terrain terrain) {
        if (database.containsValue(terrain))
            return;
        if (database.containsKey(terrain.name()))
            for (TerrainDatabaseListener listener : listeners)
                listener.onTerrainRemoved(database.get(terrain.name()));
        database.put(terrain.name(), terrain);
        for (TerrainDatabaseListener listener : listeners)
            listener.onTerrainAdded(terrain);
    }
    /** Removes an old terrain entry from the database.
     * @param terrain the old type of terrain. */
    public static void remove(Terrain terrain) {
        if (!database.containsKey(terrain.name()))
            return;
        database.remove(terrain.name());
        for (TerrainDatabaseListener listener : listeners)
            listener.onTerrainRemoved(terrain);
    }
    /** Purges all terrain entries from the database.*/
    public static void removeAll() {
        for (Terrain terrain : database.values())
            for (TerrainDatabaseListener listener : listeners)
                listener.onTerrainRemoved(terrain);
        database.clear();
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
