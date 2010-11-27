package marten.aoe.depreciated.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class TilePath {
    private List<TileDirection> directions;
    private List<TileCoordinate> tiles;
    private TileCoordinate origin;
    private UnitType pathType;
    private TileMap tileMap;
    private int lengthCache = -1;
    public TilePath(TileMap tileMap, UnitType pathType, List<TileDirection> directions, TileCoordinate origin) {
        this.tileMap = tileMap;
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
        return new TilePath(this.tileMap, this.pathType, newList, this.origin);
    }
    public int length() {
        if (this.lengthCache > -1)
            return this.lengthCache;
        int length = 0;
        for (TileCoordinate coordinate : tiles) {            
            Tile tile = null;
            try {
                tile = this.tileMap.get(coordinate);
            } catch (IndexOutOfBoundsException e) {
                return Integer.MAX_VALUE;
            }
            if (!tile.access() || tile.occupied())
                return Integer.MAX_VALUE;
            Set<TerrainFeatures> features = tile.terrain().features();
            if (pathType.equals(UnitType.AIR))
                if (features.contains(TerrainFeatures.UNFLYABLE))
                    return Integer.MAX_VALUE;
                else if (features.contains(TerrainFeatures.HARD_TO_FLY))
                    length += 2;
                else
                    length++;
            else if (pathType.equals(UnitType.GROUND))
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
    public List<TileCoordinate> coordinates() {
        return new ArrayList<TileCoordinate>(this.tiles);
    }
    public List<Tile> tiles() {
        ArrayList<Tile> pathTiles = new ArrayList<Tile>();
        for (TileCoordinate coordinate : this.tiles)
            pathTiles.add(this.tileMap.get(coordinate));
        return pathTiles;
    }
    public TileCoordinate endPoint() {
        if (this.tiles.size() == 0)
            return this.origin;
        return this.tiles.get(this.tiles.size() - 1);
    }
    public TileCoordinate origin() {
        return this.origin;
    }
    public boolean equals(Object other) {
        if (!(other instanceof TilePath))
            return false;
        TilePath otherPath = (TilePath)other;
        return this.directions.equals(otherPath.directions) && this.origin.equals(otherPath.origin) && this.pathType.equals(otherPath.pathType);
    }
    public int hashCode() {
        int hashCode = 0;
        for (TileDirection direction : this.directions)
            hashCode ^= direction.hashCode();
        hashCode ^= this.origin.hashCode();
        hashCode ^= this.pathType.hashCode();
        return hashCode;
    }
}
