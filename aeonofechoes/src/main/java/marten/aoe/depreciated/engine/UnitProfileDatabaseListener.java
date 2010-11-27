package marten.aoe.depreciated.engine;

public interface UnitProfileDatabaseListener {
    void onProfileAdded(UnitProfile unitProfile);
    void onProfileRemoved(UnitProfile unitProfile);
}
