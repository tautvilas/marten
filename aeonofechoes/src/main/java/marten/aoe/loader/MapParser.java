package marten.aoe.loader;

import marten.aoe.engine.TerrainDatabase;
import marten.aoe.engine.Tile;
import marten.aoe.engine.TileCoordinate;
import marten.aoe.engine.TileMap;

final class MapParser implements Parser {
    public void parse(DataTree data) {
        if (data.value().equals("FILE"))
            for (DataTree branch : data.branches())
                if (branch.value().equals("Map")) {
                    TileMap.removeAll();
                    for (DataTree subbranch : branch.branches()) {
                        if (subbranch.value().equals("KEYVALUE")) {
                            String key = subbranch.branches().get(0).value();
                            String value = subbranch.branches().get(1).value();
                            if (key.equals("Name"))
                                TileMap.name(value);
                        } else if (subbranch.value().equals("Tile")) {
                            int x = 0;
                            int y = 0;
                            String name = "";
                            String terrain = "";
                            for (DataTree subsubbranch : subbranch.branches())
                                if (subsubbranch.value().equals("KEYVALUE")) {
                                    String key = subbranch.branches().get(0).value();
                                    String value = subbranch.branches().get(1).value();
                                    if (key.equals("Name"))
                                        name = value;
                                    if (key.equals("X"))
                                        x = Integer.parseInt(value);
                                    if (key.equals("Y"))
                                        y = Integer.parseInt(value);
                                    if (key.equals("Terrain"))
                                        terrain = value;
                                }
                            TileMap.add(new Tile(TerrainDatabase.get(terrain), new TileCoordinate(x, y), name));
                        }
                    }                    
                }
    }
}
