package marten.aoe.dto;

import java.io.Serializable;

public final class FullMapDTO implements Serializable {
    private static final long serialVersionUID = -178599366315529060L;
    private final FullTileDTO[][] map;
    private final int width;
    private final int height;
    private final String name;
    public FullMapDTO (FullTileDTO[][] map, int width, int height, String name) {
        this.map = map;
        this.name = name;
        this.height = height;
        this.width = width;
    }
    public FullTileDTO getTileDTO (PointDTO location) {
        return this.map[location.getX()][location.getY()];
    }
    public int getWidth () {
        return this.width;
    }
    public int getHeight () {
        return this.height;
    }
    public String getName () {
        return this.name;
    }
}
