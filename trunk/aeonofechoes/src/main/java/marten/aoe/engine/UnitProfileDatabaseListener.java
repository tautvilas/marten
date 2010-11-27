package marten.aoe.engine;

public interface UnitProfileDatabaseListener {
    void onProfileAdded(UnitProfile unitProfile);
    void onProfileRemoved(UnitProfile unitProfile);
}
