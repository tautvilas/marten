package marten.aoe.proposal.dto;

import java.io.Serializable;

public final class MinimalMapDTO implements Serializable {
    private static final long serialVersionUID = 2061015657078949829L;
    private final MinimalTileDTO[][] tileMap;
    private final String name;
    private final int width;
    private final int height;
    public MinimalMapDTO (MinimalTileDTO[][] tileMap, int width, int height, String name) {
        this.tileMap = tileMap;
        this.name = name;
        this.width = width;
        this.height = height;
    }
    public MinimalTileDTO getTileDTO (Point location) {
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
    public MinimalTileDTO[][] getTileMap() {
        return this.tileMap;
    }
}
