package marten.aoe.engine;

import java.util.ArrayList;
import java.util.List;

import marten.aoe.dto.FullMapDTO;
import marten.aoe.dto.FullTileDTO;
import marten.aoe.dto.MapDTO;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;

public abstract class Map {
    private final Tile[][] map;
    private final int width;
    private final int height;
    private final String name;
    private final Engine engine;

    // Since pathfinding for a unit is a costly procedure, we will do some caching
    private PathFinder pathCache = null;

    public Map (Engine engine, String name, int width, int height) {
        this.engine = engine;
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
    public final void invokeLocalEvent (LocalEvent event, PointDTO location) {
        this.engine.invokeLocalEvent(event, this.map[location.getX()][location.getY()]);
    }
    public final void invokeLocalEventConcealUnit (LocalEvent event, PointDTO location) {
        this.engine.invokeLocalEventConcealUnit(event, this.map[location.getX()][location.getY()]);
    }
    public final void invokePlayerSpecificLocalEvent (LocalEvent event, PointDTO location, PlayerDTO player) {
        this.engine.invokePlayerSpecificLocalEvent(event, this.map[location.getX()][location.getY()], player);
    }
    public final FullMapDTO getFullDTO (PlayerDTO player) {
        FullTileDTO[][] tiles = new FullTileDTO[this.width][this.height];
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                tiles[x][y] = (this.map[x][y] != null ? this.map[x][y].getFullDTO(player) : null);
            }
        }
        return new FullMapDTO(tiles, this.width, this.height, this.name);
    }
    public final MapDTO getDTO (PlayerDTO player) {
        TileDTO[][] tiles = new TileDTO[this.width][this.height];
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                tiles[x][y] = (this.map[x][y] != null ? this.map[x][y].getDTO(player) : null);
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
    public final Tile switchTile (Tile tile) {
        PointDTO point = tile.getCoordinates();
        if (point.getX() >= 0 && point.getX() < this.width && point.getY() >= 0 && point.getY() < this.height) {
            Tile oldTile = this.map[point.getX()][point.getY()];
            if (oldTile != null) {
                Unit unit = oldTile.popUnit(PlayerDTO.SYSTEM);
                tile.pushUnit(PlayerDTO.SYSTEM, unit);
            }
            this.map[point.getX()][point.getY()] = tile;
            this.invokeLocalEvent(LocalEvent.TILE_CHANGE, point);
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
    public final boolean moveUnit (PlayerDTO player, PointDTO from, PointDTO to) {
        if (from.getX() < 0 || from.getX() >= this.width || from.getY() < 0 || from.getY() >= this.height || to.getX() < 0 || to.getX() >= this.width || to.getY() < 0 || to.getY() >= this.height) {
            return false;
        }
        Tile startTile = this.map[from.getX()][from.getY()];
        if (!startTile.isOccupied() || startTile.getUnit().getOwner() != player) {
            return false;
        }
        Unit unit = startTile.getUnit();
        if (unit.getMovementAllowance() == 0) {
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
        for (Tile pathTile : path) {
            if (pathTile != startTile) {
                pathTile.pushUnit(player, unit);
            }
            if (pathTile != finishTile) {
                pathTile.popUnit(player);
            }
        }
        return true;
    }
    public abstract void onTurnOver ();
    public abstract PointDTO getStartingPosition (PlayerDTO player);
    public abstract int getPlayerLimit ();
    /** @return all <code>Unit</code>s that belong to the given player.*/
    public final List<Unit> getAllUnits(PlayerDTO player) {
        List<Unit> answer = new ArrayList<Unit>();
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                Unit unit = this.map[x][y].getUnit();
                if (unit != null && unit.getOwner() == player) {
                    answer.add(unit);
                }
            }
        }
        return answer;
    }
}
