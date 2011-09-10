package marten.aoe.gui.widget;

import java.util.HashMap;

import marten.age.graphics.SceneGraphBranch;
import marten.age.graphics.SceneGraphChild;
import marten.age.graphics.appearance.Appearance;
import marten.age.graphics.appearance.Color;
import marten.age.graphics.geometry.Geometry;
import marten.age.graphics.geometry.primitives.Rectangle;
import marten.age.graphics.model.SimpleModel;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.texture.Texture;
import marten.age.graphics.texture.TextureLoader;
import marten.aoe.dto.TileDTO;
import marten.aoe.gui.TileImageFactory;

public class TerrainCache {

    private SceneGraphBranch<SceneGraphChild> context;

    public TerrainCache(SceneGraphBranch<SceneGraphChild> context) {
        this.context = context;
    }

    private final HashMap<String, SimpleModel> terrainCache = new HashMap<String, SimpleModel>();

    public void updateTile(TileDTO tile, TileDTO oldTile, Point displayCoords) {
        if (!this.containsType(tile)) {
            this.context.addChild(this.addFogType(tile));
            this.context.addChild(this.addType(tile));
        }
        Rectangle geometry = new Rectangle(new Dimension(64, 64), displayCoords);
        if (oldTile != null) {
            this.remove(oldTile, geometry);
        }
        this.put(tile, geometry);
    }

    private void put(TileDTO tile, Geometry geometry) {
        if (tile.getVisibility()) {
            terrainCache.get(tile.getName()).addGeometry(geometry);
        } else {
            terrainCache.get(tile.getName() + "fog").addGeometry(geometry);
        }
    }

    private void remove(TileDTO tile, Geometry geometry) {
        if (tile.getVisibility()) {
            terrainCache.get(tile.getName()).removeGeometry(geometry);
        } else {
            terrainCache.get(tile.getName() + "fog").removeGeometry(geometry);
        }
    }

    private boolean containsType(TileDTO tile) {
        return terrainCache.containsKey(tile.getName());
    }

    private SimpleModel addType(TileDTO tile) {
        String type = tile.getName();
        Texture terrain = TextureLoader.loadTexture(TileImageFactory
                .getTile(tile.getName()));
        SimpleModel sm = new SimpleModel(new Appearance(terrain));
        terrainCache.put(type, sm);
        return sm;
    }

    private SimpleModel addFogType(TileDTO tile) {
        String type = tile.getName();
        Texture terrain = TextureLoader.loadTexture(TileImageFactory
                .getTile(tile.getName()));
        Appearance fog = new Appearance(terrain);
        fog.setColor(new Color(0.5, 0.4, 0.4));
        SimpleModel sm = new SimpleModel(fog);
        terrainCache.put(type + "fog", sm);
        return sm;
    }
}
