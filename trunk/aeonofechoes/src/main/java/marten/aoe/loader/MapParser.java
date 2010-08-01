package marten.aoe.loader;

import marten.aoe.engine.Engine;
import marten.aoe.engine.TerrainDatabase;
import marten.aoe.engine.Tile;
import marten.aoe.engine.TileCoordinate;

final class MapParser {
    private MapParser() {}
    public static void parse(Engine engine, DataTree branch) {
        if (branch.value().equals("Map")) {
            for (DataTree subbranch : branch.branches()) {
                if (subbranch.value().equals("KEYVALUE")) {
                    String key = subbranch.branches().get(0).value();
                    String value = subbranch.branches().get(1).value();
                    if (key.equals("Name"))
                        engine.tileMap.name(value);
                } else if (subbranch.value().equals("Tile")) {
                    int x = 0;
                    int y = 0;
                    String name = "";
                    String terrain = "";
                    boolean access = true;
                    for (DataTree subsubbranch : subbranch.branches())
                        if (subsubbranch.value().equals("KEYVALUE")) {
                            String key = subsubbranch.branches().get(0).value();
                            String value = subsubbranch.branches().get(1).value();
                            if (key.equals("Name"))
                                name = value;
                            else if (key.equals("X"))
                                x = Integer.parseInt(value);
                            else if (key.equals("Y"))
                                y = Integer.parseInt(value);
                            else if (key.equals("Terrain"))
                                terrain = value;
                            else
                                System.err.println("Unknown option: "+key+" = "+value);
                        } else if (subsubbranch.value().equals("Inaccessible"))
                            access = false;
                        else if (subsubbranch.value().equals("Accessible"))
                            access = true;
                        else
                            System.err.println("Unknown option: "+subsubbranch.value());
                    if (TerrainDatabase.definedTerrain().contains(terrain))
                        new Tile(engine.tileMap, TerrainDatabase.get(terrain), new TileCoordinate(x, y), name, access);
                    else
                        System.err.println("Unknown terrain type: "+terrain);
                }
            }                    
        }
    }
}
