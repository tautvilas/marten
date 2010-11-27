package marten.aoe.depreciated.loader;

import marten.aoe.depreciated.engine.Engine;
import marten.aoe.depreciated.engine.TileCoordinate;

final class ClearMapParser {
    private ClearMapParser() {}
    public static void parse(Engine engine, DataTree branch) {
        if (branch.value().equals("Map")) {
            engine.tileMap.removeAll();
            for (DataTree subbranch : branch.branches()) {
                if (subbranch.value().equals("All"))
                    engine.tileMap.removeAll();
                else if (subbranch.value().equals("Tile")) {
                    int x = 0;
                    int y = 0;
                    for (DataTree subsubbranch : subbranch.branches())
                        if (subsubbranch.value().equals("KEYVALUE")) {
                            String key = subsubbranch.branches().get(0).value();
                            String value = subsubbranch.branches().get(1).value();
                            if (key.equals("X"))
                                x = Integer.parseInt(value);
                            else if (key.equals("Y"))
                                y = Integer.parseInt(value);
                            else
                                System.err.println("Unknown option: "+key+" = "+value);
                        }
                        else
                            System.err.println("Unknown option: "+subsubbranch.value());
                    engine.tileMap.remove(new TileCoordinate(x, y));
                }
            }                    
        }
    }
}
