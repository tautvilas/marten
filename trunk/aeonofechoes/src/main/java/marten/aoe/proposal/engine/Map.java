package marten.aoe.proposal.engine;

import marten.aoe.proposal.dto.MapDTO;
import marten.aoe.proposal.dto.MinimalMapDTO;
import marten.aoe.proposal.dto.MinimalTileDTO;
import marten.aoe.proposal.dto.Point;
import marten.aoe.proposal.dto.TileDTO;

public abstract class Map {
    private final Tile[][] map;
    private final int width;
    private final int height;
    private final String name;

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
    public final MapDTO getDTO (Player player) {
        TileDTO[][] tiles = new TileDTO[this.width][this.height];
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                tiles[x][y] = (this.map[x][y] != null ? this.map[x][y].getDTO(player) : null);
            }
        }
        return new MapDTO(tiles, this.width, this.height, this.name);
    }
    public final MinimalMapDTO getMinimalDTO (Player player) {
        MinimalTileDTO[][] tiles = new MinimalTileDTO[this.width][this.height];
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                tiles[x][y] = (this.map[x][y] != null ? this.map[x][y].getMinimalDTO(player) : null);
            }
        }
        return new MinimalMapDTO(tiles, this.width, this.height, this.name);
    }
    public final Tile getTile (Point point) {
        if (point.getX() >= 0 && point.getX() < this.width && point.getY() >= 0 && point.getY() < this.height) {
            return map[point.getX()][point.getY()];
        }
        return null;
    }
    public final Tile switchTile (Point point, Tile tile) {
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
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (this.map[x][y] != null) {
                    this.map[x][y].turnOver();
                }
            }
        }
        this.onTurnOver();
    }
    public abstract void onTurnOver ();
    public abstract Point getStartingPosition (Player player);
    public abstract int getPlayerLimit ();
}
