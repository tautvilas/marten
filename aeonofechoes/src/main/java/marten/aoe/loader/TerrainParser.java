package marten.aoe.loader;

import java.util.EnumSet;

import marten.aoe.engine.Terrain;
import marten.aoe.engine.TerrainFeatures;

final class TerrainParser implements Parser {
    public TerrainParser() {}
    public void parse (DataTree data) {
        if (data.value().equals("FILE"))
            for (DataTree branch : data.branches())
                if (branch.value().equals("Terrain")) {
                    String name = "";
                    EnumSet<TerrainFeatures> features = EnumSet.noneOf(TerrainFeatures.class);
                    for (DataTree subbranch : branch.branches())
                        if (subbranch.value().equals("KEYVALUE")) {
                            String key = subbranch.branches().get(0).value();
                            String value = subbranch.branches().get(1).value();
                            if (key.equals("Name"))
                                name = value;
                        } else if (subbranch.value().equals("Features"))
                            for (DataTree subsubbranch : subbranch.branches()) {
                                TerrainFeatures feature = TerrainFeatures.fromString(subsubbranch.value());
                                if (feature != null)
                                    features.add(feature);
                            }
                    new Terrain(features, name);
                }
    }
}
