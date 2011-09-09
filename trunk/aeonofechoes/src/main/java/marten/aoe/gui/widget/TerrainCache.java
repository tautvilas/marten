package marten.aoe.gui.widget;

import java.util.HashMap;

import marten.age.graphics.SceneGraphBranch;
import marten.age.graphics.appearance.Appearance;
import marten.age.graphics.appearance.Color;
import marten.age.graphics.geometry.Geometry;
import marten.age.graphics.model.SimpleModel;
import marten.age.graphics.texture.Texture;
import marten.age.graphics.texture.TextureLoader;
import marten.aoe.dto.TileDTO;
import marten.aoe.gui.TileImageFactory;

public class TerrainCache {

    private SceneGraphBranch context;

    public TerrainCache(SceneGraphBranch context) {
        this.context = context;
    }

    private static final HashMap<String, SimpleModel> terrainCache = new HashMap<String, SimpleModel>();

    public static void put(TileDTO tile, Geometry geometry) {
        if (tile.getVisibility()) {
            terrainCache.get(tile.getName()).addGeometry(geometry);
        } else {
            terrainCache.get(tile.getName() + "fog").addGeometry(geometry);
        }
    }

    public static void remove(TileDTO tile, Geometry geometry) {
        if (tile.getVisibility()) {
            terrainCache.get(tile.getName()).removeGeometry(geometry);
        } else {
            terrainCache.get(tile.getName() + "fog").removeGeometry(geometry);
        }
    }

    public static boolean containsType(TileDTO tile) {
        return terrainCache.containsKey(tile.getName());
    }

    public static SimpleModel addType(TileDTO tile) {
        String type = tile.getName();
        Texture terrain = TextureLoader.loadTexture(TileImageFactory
                .getTile(tile.getName()));
        SimpleModel sm = new SimpleModel(new Appearance(terrain));
        terrainCache.put(type, sm);
        return sm;
    }

    public static SimpleModel addFogType(TileDTO tile) {
        String type = tile.getName();
        Texture terrain = TextureLoader.loadTexture(TileImageFactory
                .getTile(tile.getName()));
        Appearance fog = new Appearance(terrain);
        fog.setColor(new Color(0.5, 0.4, 0.4));
        SimpleModel sm = new SimpleModel(fog);
        terrainCache.put(type + "fog", sm);
        return sm;
    }

    public static void clear() {
        terrainCache.clear();
    }
}
