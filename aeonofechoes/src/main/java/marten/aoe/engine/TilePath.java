package marten.aoe.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class TilePath {
    private List<TileDirection> directions;
    private List<TileCoordinate> tiles;
    private TileCoordinate origin;
    private TilePathType pathType;
    private int lengthCache = -1;
    public TilePath(TilePathType pathType, List<TileDirection> directions, TileCoordinate origin) {
        this.directions = new ArrayList<TileDirection>(directions);
        this.tiles = new ArrayList<TileCoordinate>();
        this.origin = origin;
        this.pathType = pathType;
        TileCoordinate pointer = origin;
        for (TileDirection direction : directions) {
            pointer = pointer.adjacent(direction);
            this.tiles.add(pointer);
        }
    }
    public TilePath extendBy(TileDirection direction) {
        List<TileDirection> newList = new ArrayList<TileDirection>(this.directions);
        newList.add(direction);
        return new TilePath(this.pathType, newList, this.origin);
    }
    public int length() {
        if (this.lengthCache > -1)
            return this.lengthCache;
        int length = 0;
        for (TileCoordinate coordinate : tiles) {
            Tile tile = TileMap.get(coordinate);
            if (tile == null || !tile.access() || tile.occupied())
                return Integer.MAX_VALUE;
            Set<TerrainFeatures> features = tile.terrain().features();
            if (pathType.equals(TilePathType.AIR))
                if (features.contains(TerrainFeatures.UNFLYABLE))
                    return Integer.MAX_VALUE;
                else if (features.contains(TerrainFeatures.HARD_TO_FLY))
                    length += 2;
                else
                    length++;
            else if (pathType.equals(TilePathType.GROUND))
                if (features.contains(TerrainFeatures.UNWALKABLE))
                    return Integer.MAX_VALUE;
                else if (features.contains(TerrainFeatures.HARD_TO_WALK))
                    length += 2;
                else
                    length++;
            else if (features.contains(TerrainFeatures.UNSWIMMABLE))
                return Integer.MAX_VALUE;
            else if (features.contains(TerrainFeatures.HARD_TO_SWIM))
                length += 2;
            else
                length++;
        }
        this.lengthCache = length;
        return length;
    }
    public List<TileDirection> directions() {
        return new ArrayList<TileDirection>(this.directions);
    }
    public List<TileCoordinate> tiles() {
        return new ArrayList<TileCoordinate>(this.tiles());
    }
    public TileCoordinate endPoint() {
        if (this.tiles.size() == 0)
            return this.origin;
        return this.tiles.get(this.tiles.size() - 1);
    }
}
