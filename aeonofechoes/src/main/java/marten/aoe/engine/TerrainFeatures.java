package marten.aoe.engine;

public enum TerrainFeatures {
    DIFFICULT,  // Ground units should slow down
    IMPASSABLE, // Ground units cannot enter this tile
    COVERED,    // Some of the ground units may benefit defensively
    HIGH,       // Ground units uphill may benefit defensively or offensively
    HIGHER,     // Same, but units on "higher" have benefit over "high" (overrides high)
    WATER       // Marine ground units may enter this tile (overrides difficult and impassable for marine units only)
    // XXX: could be extended with additional features should necessity arise.
}
