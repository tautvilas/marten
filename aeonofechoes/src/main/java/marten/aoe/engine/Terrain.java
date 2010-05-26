package marten.aoe.engine;

import java.util.EnumSet;
import java.util.Set;

public final class Terrain {
    private Set<TerrainFeatures> features;
    private String name;
    public Terrain(Set<TerrainFeatures> features, String name) {
        this.features = features;
        this.name = name;
        TerrainDatabase.add(this);
    }
    public Set<TerrainFeatures> features() {
        return EnumSet.copyOf(this.features);
    }
    public String name() {
        return this.name;
    }
    public boolean equals(Object other) {
        if (!(other instanceof Terrain))
            return false;
        Terrain otherTile = (Terrain)other;
        return otherTile.name.equals(this.name) && otherTile.features.equals(this.features); 
    }
}
