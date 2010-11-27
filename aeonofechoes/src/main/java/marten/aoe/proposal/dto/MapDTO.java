package marten.aoe.proposal.dto;

import java.io.Serializable;

public final class MapDTO implements Serializable {
    private static final long serialVersionUID = -178599366315529060L;
    private final TileDTO[][] map;
    private final int width;
    private final int height;
    private final String name;
    public MapDTO (TileDTO[][] map, int width, int height, String name) {
        this.map = map;
        this.name = name;
        this.height = height;
        this.width = width;
    }
    public TileDTO getTileDTO (Point location) {
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
