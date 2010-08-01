package marten.aoe.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class PathFinder {
    private Map<TileCoordinate, TilePath> pathMap = new HashMap<TileCoordinate, TilePath>();
    public PathFinder(TileMap tileMap, TileCoordinate origin, UnitType type, int maxDistance) {
        TilePath trivialPath = new TilePath(tileMap, type, new ArrayList<TileDirection>(), origin);
        this.pathMap.put(origin, trivialPath);
        this.generatePaths(trivialPath, maxDistance);
    }
    private void generatePaths(TilePath path, int maxDistance) {
        for (TileDirection direction : TileDirection.values()) {
            TilePath newPath = path.extendBy(direction);
            if (newPath.length() > maxDistance)
                continue;
            TilePath savedPath = this.pathMap.get(newPath.endPoint());
            if (savedPath == null || savedPath.length() > newPath.length()) {
                this.pathMap.put(newPath.endPoint(), newPath);
                this.generatePaths(newPath, maxDistance);
            }                
        }            
    }
    public TilePath findPath(TileCoordinate to) {
        return this.pathMap.get(to);
    }
}
