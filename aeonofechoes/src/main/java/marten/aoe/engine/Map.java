package marten.aoe.engine;

import java.util.ArrayList;
import java.util.List;

import marten.aoe.dto.MapDTO;
import marten.aoe.dto.MapMetaDTO;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.engine.loader.UnitLoader;

public abstract class Map {
    private final Tile[][] map;
    private final MapMetaDTO meta;
    private final Engine owner;
    // Since pathfinding for a unit is a costly procedure, we will do some caching
    private PathFinder pathCache = null;

    public Map (Engine engine, MapMetaDTO meta) {
        this.meta = meta;
        this.map = new Tile[meta.getWidth()][meta.getHeight()];
        for (int x = 0; x < meta.getWidth(); x++) {
            for (int y = 0; y < meta.getHeight(); y++) {
                this.map[x][y] = null;
            }
        }
        this.owner = engine;
    }
    public final MapMetaDTO getMeta () {
        return this.meta;
    }
    public final Engine getOwner () {
        return this.owner;
    }
    public final MapDTO getDTO (PlayerDTO player) {
        TileDTO[][] tiles = new TileDTO[this.meta.getWidth()][this.meta.getHeight()];
        for (int x = 0; x < this.meta.getWidth(); x++) {
            for (int y = 0; y < this.meta.getHeight(); y++) {
                tiles[x][y] = (this.map[x][y] != null ? this.map[x][y].getDTO(player) : null);
            }
        }
        return new MapDTO(tiles, this.meta);
    }
    public final Tile getTile (PointDTO point) {
        if (point.getX() >= 0 && point.getX() < this.meta.getWidth() && point.getY() >= 0 && point.getY() < this.meta.getHeight()) {
            return this.map[point.getX()][point.getY()];
        }
        return null;
    }
    public final Tile switchTile (Tile tile) {
        PointDTO point = tile.getCoordinates();
        if (point.getX() >= 0 && point.getX() < this.meta.getWidth() && point.getY() >= 0 && point.getY() < this.meta.getHeight()) {
            Tile oldTile = this.map[point.getX()][point.getY()];
            if (oldTile != null) {
                Unit unit = oldTile.popUnit(PlayerDTO.SYSTEM);
                tile.pushUnit(PlayerDTO.SYSTEM, unit);
            }
            this.map[point.getX()][point.getY()] = tile;
            return oldTile;
        }
        throw new IllegalArgumentException("The requested location is out of map bounds.");
    }
    public final void endTurn () {
        for (int x = 0; x < this.meta.getWidth(); x++) {
            for (int y = 0; y < this.meta.getHeight(); y++) {
                if (this.map[x][y] != null) {
                    this.map[x][y].turnOver();
                }
            }
        }
        this.pathCache = null;
        this.onTurnOver();
    }
    public final boolean moveUnit (PlayerDTO player, PointDTO from, PointDTO to) {
        if (from.getX() < 0 || from.getX() >= this.meta.getWidth() || from.getY() < 0 || from.getY() >= this.meta.getHeight() || to.getX() < 0 || to.getX() >= this.meta.getWidth() || to.getY() < 0 || to.getY() >= this.meta.getHeight()) {
            throw new IllegalArgumentException("Requested coordinates are out of bounds of the map.");
        }
        if (from.equals(to)) {
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
    /** @return all <code>Unit</code>s that belong to the given player.*/
    public final List<Unit> getAllUnits(PlayerDTO player) {
        List<Unit> answer = new ArrayList<Unit>();
        for (int x = 0; x < this.meta.getWidth(); x++) {
            for (int y = 0; y < this.meta.getHeight(); y++) {
                Unit unit = this.map[x][y].getUnit();
                if (unit != null && unit.getOwner() == player) {
                    answer.add(unit);
                }
            }
        }
        return answer;
    }
    public void recalculateVisibility(PlayerDTO player) {
        boolean[][] currentVisibilityMatrix = new boolean[this.meta.getWidth()][this.meta.getHeight()];
        boolean[][] currentExplorationMatrix = new boolean[this.meta.getWidth()][this.meta.getHeight()];
        boolean[][] currentDetectionMatrix = new boolean[this.meta.getWidth()][this.meta.getHeight()];
        boolean[][] newVisibilityMatrix = new boolean[this.meta.getWidth()][this.meta.getHeight()];
        boolean[][] newExplorationMatrix = new boolean[this.meta.getWidth()][this.meta.getHeight()];
        boolean[][] newDetectionMatrix = new boolean[this.meta.getWidth()][this.meta.getHeight()];
        for (int x = 0; x < this.meta.getWidth(); x++) {
            for (int y = 0; y < this.meta.getHeight(); y++) {
                currentDetectionMatrix[x][y] = this.map[x][y].isDetected(player);
                currentVisibilityMatrix[x][y] = this.map[x][y].isVisible(player);
                newDetectionMatrix[x][y] = newVisibilityMatrix[x][y] = false;
                newExplorationMatrix[x][y] = currentExplorationMatrix[x][y] = this.map[x][y].isExplored(player);
            }
        }
        for (Unit unit : this.getAllUnits(player)) {
            for (Tile tile : unit.getLocation().neighbors(1)) {
                PointDTO coordinates = tile.getCoordinates();
                newDetectionMatrix[coordinates.getX()][coordinates.getY()] = true;
            }
            for (Tile tile : unit.getLocation().neighbors(unit.getDetectionRange())) {
                PointDTO coordinates = tile.getCoordinates();
                if (unit.isObserving() || !tile.hasAnythingCloaked(player)) {
                    newDetectionMatrix[coordinates.getX()][coordinates.getY()] = true;
                }
                newExplorationMatrix[coordinates.getX()][coordinates.getY()] = newVisibilityMatrix[coordinates.getX()][coordinates.getY()] = true;
            }
        }
        for (int x = 0; x < this.meta.getWidth(); x++) {
            for (int y = 0; y < this.meta.getHeight(); y++) {
                if (!currentVisibilityMatrix[x][y] && newVisibilityMatrix[x][y]) {
                    this.map[x][y].setVisible(player);
                }
                if (currentVisibilityMatrix[x][y] && !newVisibilityMatrix[x][y]) {
                    this.map[x][y].setInvisible(player);
                }
                if (!currentExplorationMatrix[x][y] && newExplorationMatrix[x][y]) {
                    this.map[x][y].setExplored(player);
                }
                if (!currentDetectionMatrix[x][y] && newDetectionMatrix[x][y]) {
                    this.map[x][y].setDetected(player);
                }
                if (currentDetectionMatrix[x][y] && !newDetectionMatrix[x][y]) {
                    this.map[x][y].setUndetected(player);
                }
            }
        }
    }
    /** Allows spawning units during the scenarios or their initialization.
     * @return {@code true} if the unit was spawned successfully.
     * @param player - the player who will control the unit after spawning.
     * @param name - the class name of the unit to be created.
     * @param at - the location where the unit will spawn.*/
    public boolean spawnUnit (PlayerDTO player, String name, PointDTO at) {
        UnitLoader.loadUnit(name, player, this.getTile(at));
        return true;
    }
    public void selectUnit(PlayerDTO player, PointDTO location) {
        this.pathCache = new PathFinder(this, this.map[location.getX()][location.getY()]);
    }
}
