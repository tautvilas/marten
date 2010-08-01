package marten.aoe.engine;

import java.util.EnumSet;
import java.util.Set;

/** This class defines the name and logical features of a certain type of terrain */
public final class Terrain {
    private Set<TerrainFeatures> features;
    private String name;
    /** Creates a new type of terrain with defined characteristics and registers it in the database.
     * @param features the inherent features of this terrain
     * @param name the name of the terrain
     * @throws IllegalArgumentException if the name of the terrain being created is already taken by a different description.*/
    public Terrain(TerrainDatabase database, Set<TerrainFeatures> features, String name) {
        this.features = features;
        this.name = name;
        database.add(this);
    }
    /** @return the set of features inherent to this terrain. */
    public Set<TerrainFeatures> features() {
        return EnumSet.copyOf(this.features);
    }
    /** @return the name of this terrain */
    public String name() {
        return this.name;
    }
    /** @return <code>true</code> if and only if <code>other</code> is an instance of <code>Terrain</code> and has exactly the same name and exactly the same set of features.
     * @param other the object this terrain is compared to. */
    public boolean equals(Object other) {
        if (!(other instanceof Terrain))
            return false;
        Terrain otherTile = (Terrain)other;
        return otherTile.name.equals(this.name) && otherTile.features.equals(this.features); 
    }
    /** @return the hashcode of this instance (the sum of hashcodes of the publicly accessible fields)*/
    public int hashCode() {
        return this.name.hashCode() + this.features.hashCode();
    }
}
