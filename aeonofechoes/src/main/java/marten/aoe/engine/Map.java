package marten.aoe.engine;

import java.util.List;

import marten.aoe.dto.FullMapDTO;
import marten.aoe.dto.FullTileDTO;
import marten.aoe.dto.MapDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;

public abstract class Map {
    private final Tile[][] map;
    private final int width;
    private final int height;
    private final String name;

    // Since pathfinding for a unit is a costly procedure, we will do some caching
    private PathFinder pathCache = null;

    public Map (String name, int width, int height) {
        this.map = new Tile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.map[x][y] = null;
            }
        }
        this.width = width;
        this.height = height;
        this.name = name;
    }
    public final int getWidth () {
        return this.width;
    }
    public final int getHeight () {
        return this.height;
    }
    public final String getName () {
        return this.name;
    }
    public final FullMapDTO getDTO (Player player) {
        FullTileDTO[][] tiles = new FullTileDTO[this.width][this.height];
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                tiles[x][y] = (this.map[x][y] != null ? this.map[x][y].getDTO(player) : null);
            }
        }
        return new FullMapDTO(tiles, this.width, this.height, this.name);
    }
    public final MapDTO getMinimalDTO (Player player) {
        TileDTO[][] tiles = new TileDTO[this.width][this.height];
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                tiles[x][y] = (this.map[x][y] != null ? this.map[x][y].getMinimalDTO(player) : null);
            }
        }
        return new MapDTO(tiles, this.width, this.height, this.name);
    }
    public final Tile getTile (PointDTO point) {
        if (point.getX() >= 0 && point.getX() < this.width && point.getY() >= 0 && point.getY() < this.height) {
            return this.map[point.getX()][point.getY()];
        }
        return null;
    }
    public final Tile switchTile (PointDTO point, Tile tile) {
        if (point.getX() >= 0 && point.getX() < this.width && point.getY() >= 0 && point.getY() < this.height) {
            Tile oldTile = this.map[point.getX()][point.getY()];
            if (oldTile != null) {
                Unit unit = oldTile.popUnit(Player.SYSTEM);
                tile.pushUnit(Player.SYSTEM, unit);
            }
            this.map[point.getX()][point.getY()] = tile;
            return oldTile;
        }
        return null;
    }
    public final void endTurn () {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                if (this.map[x][y] != null) {
                    this.map[x][y].turnOver();
                }
            }
        }
        this.onTurnOver();
    }
    public final boolean moveUnit (Player player, PointDTO from, PointDTO to) {
        if (from.getX() < 0 || from.getX() >= this.width || from.getY() < 0 || from.getY() >= this.height || to.getX() < 0 || to.getX() >= this.width || to.getY() < 0 || to.getY() >= this.height) {
            return false;
        }
        Tile startTile = this.map[from.getX()][from.getY()];
        if (!startTile.isOccupied() || startTile.getUnit().getOwner() != player) {
            return false;
        }
        Tile finishTile = this.map[to.getX()][to.getY()];
        if (this.pathCache == null || this.pathCache.getOrigin() != startTile) {
            this.pathCache = new PathFinder(this, startTile);
        }
        List<Tile> path = this.pathCache.findPathTo(finishTile);
        if (path == null) {
            return false;
        }
        Unit unit = startTile.popUnit(player);
        for (Tile pathTile : path) {
            pathTile.pushUnit(player, unit);
            if (pathTile != finishTile) {
                pathTile.popUnit(player);
            }
        }
        return true;
    }
    public abstract void onTurnOver ();
    public abstract PointDTO getStartingPosition (Player player);
    public abstract int getPlayerLimit ();
}
