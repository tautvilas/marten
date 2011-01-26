package marten.aoe.obsolete.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class UnitProfileDatabase {
    private Map<String, UnitProfile> database = new HashMap<String, UnitProfile>();
    private List<UnitProfileDatabaseListener> listeners = new ArrayList<UnitProfileDatabaseListener>();
    public UnitProfileDatabase() {}
    public void add(UnitProfile profile) {
        if (this.database.containsValue(profile))
            return;
        if (this.database.containsKey(profile.name()))
            for (UnitProfileDatabaseListener listener : this.listeners)
                listener.onProfileRemoved(this.database.get(profile.name()));
        this.database.put(profile.name(), profile);
        for (UnitProfileDatabaseListener listener : this.listeners)
            listener.onProfileAdded(profile);
    }
    public void remove(String profile) {
        if (!this.database.containsKey(profile))
            return;
        UnitProfile removedUnitProfile = this.database.remove(profile);
        for (UnitProfileDatabaseListener listener : this.listeners)
            listener.onProfileRemoved(removedUnitProfile);
    }
    public void removeAll() {
        for (UnitProfile unitProfile : this.database.values())
            for (UnitProfileDatabaseListener listener : this.listeners)
                listener.onProfileRemoved(unitProfile);
        this.database.clear();
    }
    public void addListener(UnitProfileDatabaseListener listener) {
        this.listeners.add(listener);
    }
    public void removeListener(UnitProfileDatabaseListener listener) {
        this.listeners.remove(listener);
    }
    public UnitProfile get(String name) {
        return this.database.get(name);
    }
    public Set<String> definedUnitProfiles() {
        return this.database.keySet();
    }
}
