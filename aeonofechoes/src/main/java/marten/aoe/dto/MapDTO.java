package marten.aoe.dto;

import java.io.Serializable;

public final class MapDTO implements Serializable {
    private static final long serialVersionUID = 2061015657078949829L;
    private final TileDTO[][] tileMap;
    private final String name;
    private final int width;
    private final int height;
    public MapDTO (TileDTO[][] tileMap, int width, int height, String name) {
        this.tileMap = tileMap;
        this.name = name;
        this.width = width;
        this.height = height;
    }
    public TileDTO getTileDTO (PointDTO location) {
        return this.tileMap[location.getX()][location.getY()];
    }
    public String getName () {
        return this.name;
    }
    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }
    public TileDTO[][] getTileMap() {
        return this.tileMap;
    }
}
