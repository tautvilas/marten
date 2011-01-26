package marten.aoe.obsolete.loader;

import java.util.EnumSet;

import marten.aoe.obsolete.engine.Engine;
import marten.aoe.obsolete.engine.Terrain;
import marten.aoe.obsolete.engine.TerrainFeatures;

final class TerrainParser {
    private TerrainParser() {}
    public static void parse (Engine engine, DataTree branch) {
        if (branch.value().equals("Terrain")) {
            String name = "";
            EnumSet<TerrainFeatures> features = EnumSet.noneOf(TerrainFeatures.class);
            for (DataTree subbranch : branch.branches())
                if (subbranch.value().equals("KEYVALUE")) {
                    String key = subbranch.branches().get(0).value();
                    String value = subbranch.branches().get(1).value();
                    if (key.equals("Name"))
                        name = value;
                    else
                        System.err.println("Unknown option: "+key+" = "+value);
                } else if (subbranch.value().equals("Features"))
                    for (DataTree subsubbranch : subbranch.branches()) {
                        TerrainFeatures feature = TerrainFeatures.fromString(subsubbranch.value());
                        if (feature != null)
                            features.add(feature);
                    }
                else
                    System.err.println("Unknown option: "+subbranch.value());
            new Terrain(engine.terrain, features, name);
        }
    }
}
