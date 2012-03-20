package marten.aoe.engine.core;

import java.util.ArrayList;
import java.util.List;

import marten.aoe.data.units.UnitFactory;
import marten.aoe.data.units.Units;
import marten.aoe.dto.MapDTO;
import marten.aoe.dto.MapMetaDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.engine.Engine;
import marten.aoe.engine.PathFinder;

public abstract class Map {
    private final Tile[][] map;
    private final MapMetaDTO meta;
    private final Engine owner;
    private int unitId = 0;
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
    public final MapDTO getDTO (Player player) {
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
                Unit unit = oldTile.popUnit(Player.SYSTEM);
                tile.pushUnit(Player.SYSTEM, unit);
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
    public final boolean moveUnit (Player player, PointDTO from, PointDTO to) {
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
    public final List<Unit> getAllUnits(Player player) {
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
    public void recalculateVisibility(Player player) {
        int width = this.meta.getWidth();
        int height = this.meta.getHeight();
        boolean[][] currentVisibilityMatrix = new boolean[width][height];
        boolean[][] currentExplorationMatrix = new boolean[width][height];
        boolean[][] currentDetectionMatrix = new boolean[width][height];
        boolean[][] currentPowerMatrix = new boolean[width][height];
        boolean[][] newVisibilityMatrix = new boolean[width][height];
        boolean[][] newExplorationMatrix = new boolean[width][height];
        boolean[][] newDetectionMatrix = new boolean[width][height];
        boolean[][] newPowerMatrix = new boolean[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                currentDetectionMatrix[x][y] = this.map[x][y].isDetected(player);
                currentVisibilityMatrix[x][y] = this.map[x][y].isVisible(player);
                currentPowerMatrix[x][y] = this.map[x][y].isPowered(player);
                newDetectionMatrix[x][y] = newVisibilityMatrix[x][y] = newPowerMatrix[x][y] = false;
                newExplorationMatrix[x][y] = currentExplorationMatrix[x][y] = this.map[x][y].isExplored(player);
            }
        }
        for (Unit unit : this.getAllUnits(player)) {
            Tile location = unit.getLocation();
            for (Tile tile : location.neighbors(unit.getPowerupRange())) {
                int x = tile.getCoordinates().getX();
                int y = tile.getCoordinates().getY();
                newPowerMatrix[x][y] = true;
            }
            for (Tile tile : location.neighbors(1)) {
                int x = tile.getCoordinates().getX();
                int y = tile.getCoordinates().getY();
                newDetectionMatrix[x][y] = true;
            }
            for (Tile tile : location.neighbors(unit.getDetectionRange())) {
                int x = tile.getCoordinates().getX();
                int y = tile.getCoordinates().getY();
                if (unit.isObserving() || !tile.hasAnythingCloaked(player)) {
                    newDetectionMatrix[x][y] = true;
                }
                newExplorationMatrix[x][y] = newVisibilityMatrix[x][y] = true;
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!currentVisibilityMatrix[x][y] && newVisibilityMatrix[x][y]) {
                    this.map[x][y].setVisible(player);
                }
                if (currentVisibilityMatrix[x][y] && !newVisibilityMatrix[x][y]) {
                    this.map[x][y].setInvisible(player);
                }
                if (!currentPowerMatrix[x][y] && newPowerMatrix[x][y]) {
                    this.map[x][y].setPowered(player);
                }
                if (currentPowerMatrix[x][y] && !newPowerMatrix[x][y]) {
                    this.map[x][y].setUnpowered(player);
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
    public boolean spawnUnit (Player player, Units name, PointDTO at) {
        new Unit(this.unitId++, UnitFactory.getUnit(name), this.getTile(at), player);
        return true;
    }
    public void selectUnit(Player player, PointDTO location) {
        this.pathCache = new PathFinder(this, this.map[location.getX()][location.getY()]);
    }
}
