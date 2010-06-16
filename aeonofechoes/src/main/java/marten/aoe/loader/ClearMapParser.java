package marten.aoe.loader;

import marten.aoe.engine.TileCoordinate;
import marten.aoe.engine.TileMap;

final class ClearMapParser {
    private ClearMapParser() {}
    public static void parse(DataTree branch) {
        if (branch.value().equals("Map")) {
            TileMap.removeAll();
            for (DataTree subbranch : branch.branches()) {
                if (subbranch.value().equals("All"))
                    TileMap.removeAll();
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
                    TileMap.remove(new TileCoordinate(x, y));
                }
            }                    
        }
    }
}
