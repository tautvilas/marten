package marten.aoe.gui.widget;

import marten.age.graphics.geometry.Geometry;
import marten.age.graphics.geometry.primitives.Rectangle;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.aoe.dto.TileDTO;

public class TileWidget {

    private TileDTO tile;
    private Geometry geometry;

    public TileWidget(TileDTO tile, Point position, Dimension dimension) {
        this.geometry = new Rectangle(dimension, position);
        this.tile = tile;
        TerrainCache.put(tile, geometry);
    }

    public void update(TileDTO tile) {
        TerrainCache.remove(this.tile, geometry);
        TerrainCache.put(tile, geometry);
        this.tile = tile;
    }

    public TileDTO getDto() {
        return this.tile;
    }
}

