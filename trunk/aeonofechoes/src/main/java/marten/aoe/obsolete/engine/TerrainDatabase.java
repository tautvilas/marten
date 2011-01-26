package marten.aoe.obsolete.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** The database to store all configurations of terrain currently used in the game.
 * @author Petras Ražanskas */
public final class TerrainDatabase {
    private Map<String, Terrain> database = new HashMap<String, Terrain>();
    private List<TerrainDatabaseListener> listeners = new ArrayList<TerrainDatabaseListener>();
    public TerrainDatabase() {}
    /** Adds a new terrain entry to the database.
     * If there is a pre-existing terrain type with the same name, it is implicitly removed unless it is equivalent to the terrain type being added.
     * @param terrain the new type of terrain. */
    public void add(Terrain terrain) {
        if (this.database.containsValue(terrain))
            return;
        if (this.database.containsKey(terrain.name()))
            for (TerrainDatabaseListener listener : this.listeners)
                listener.onTerrainRemoved(this.database.get(terrain.name()));
        this.database.put(terrain.name(), terrain);
        for (TerrainDatabaseListener listener : this.listeners)
            listener.onTerrainAdded(terrain);
    }
    /** Removes an old terrain entry from the database.
     * @param terrain the name of the terrain being removed. */
    public void remove(String terrain) {
        if (!this.database.containsKey(terrain))
            return;
        Terrain removedTerrain = this.database.remove(terrain);
        for (TerrainDatabaseListener listener : this.listeners)
            listener.onTerrainRemoved(removedTerrain);
    }
    /** Purges all terrain entries from the database.*/
    public void removeAll() {
        for (Terrain terrain : this.database.values())
            for (TerrainDatabaseListener listener : this.listeners)
                listener.onTerrainRemoved(terrain);
        this.database.clear();
    }
    /** Adds a new listener to track information on database changes 
     * @param listener the new listener to be connected to the database.*/
    public void addListener(TerrainDatabaseListener listener) {
        this.listeners.add(listener);
    }
    /** Removes a listener which no longer wishes to track database changes 
     * @param listener the listener to be disconnected from the database.*/
    public void removeListener(TerrainDatabaseListener listener) {
        this.listeners.remove(listener);
    }
    /** @return the complete definition of terrain with the given name */
    public Terrain get(String name) {
        return this.database.get(name);
    }
    /** @return the complete list of currently defined terrain types */
    public Set<String> definedTerrain() {
        return this.database.keySet();
    }
}
