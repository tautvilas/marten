package marten.aoe.obsolete.loader;

import marten.aoe.obsolete.engine.Engine;
import marten.aoe.obsolete.engine.Tile;
import marten.aoe.obsolete.engine.TileCoordinate;
import marten.aoe.obsolete.engine.Unit;

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
                    String unit = null;
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
                            else if (key.equals("Unit"))
                                unit = value;
                            else
                                System.err.println("Unknown option: "+key+" = "+value);
                        } else if (subsubbranch.value().equals("Inaccessible"))
                            access = false;
                        else if (subsubbranch.value().equals("Accessible"))
                            access = true;
                        else
                            System.err.println("Unknown option: "+subsubbranch.value());
                    if (engine.terrain.definedTerrain().contains(terrain))
                        new Tile(engine.tileMap, engine.terrain.get(terrain), new TileCoordinate(x, y), name, access);
                    else
                        System.err.println("Unknown terrain type: "+terrain);
                    if (unit != null)
                        if (engine.unitProfiles.definedUnitProfiles().contains(unit))
                            new Unit(engine.tileMap, new TileCoordinate(x, y), engine.unitProfiles.get(unit));
                        else
                            System.err.println("Unknown unit type: "+unit);
                }
            }                    
        }
    }
}
