package marten.aoe.gui;

import java.util.HashMap;

import marten.age.graphics.SceneGraphBranch;
import marten.age.graphics.SceneGraphChild;
import marten.age.graphics.appearance.Appearance;
import marten.age.graphics.appearance.Color;
import marten.age.graphics.geometry.Geometry;
import marten.age.graphics.geometry.primitives.Rectangle;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.model.SimpleModel;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.primitives.TextureCoords;
import marten.age.graphics.texture.Texture;
import marten.age.graphics.texture.TextureLoader;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;

public class TerrainDrawer {

    private SceneGraphBranch<SceneGraphChild> context;

    public TerrainDrawer(SceneGraphBranch<SceneGraphChild> context) {
        this.context = context;
    }

    private final HashMap<String, SimpleModel> terrainCache = new HashMap<String, SimpleModel>();
    private final HashMap<PointDTO, String> idCache = new HashMap<PointDTO, String>();

    public void updateTile(TileDTO tile, Point displayCoords, TileDTO[] surrounds) {
        String tileId = TileImageFactory.getTileGuiId(tile, surrounds);
        if (!this.containsType(tileId)) {
            ImageData tileImage = TileImageFactory.getTile(tile, surrounds);
            if (tile.getVisibility()) {
                if (tile.isPowered()) {
                    this.context.addChild(this.createPoweredType(tileImage, tileId));
                } else {
                    this.context.addChild(this.cteateVisbileType(tileImage, tileId));
                }
            } else {
                this.context.addChild(this.createFogType(tileImage, tileId));
            }
        }
        TextureCoords coords = this.terrainCache.get(tileId).getAppearance().getTexture().getCoords();
        Rectangle geometry = new Rectangle(displayCoords, new Dimension(84, 72), coords);
        this.remove(tile.getCoordinates(), geometry);
        this.put(tile.getCoordinates(), tileId, geometry);
    }

    private void put(PointDTO coords, String tileId, Geometry geometry) {
        SimpleModel sm = terrainCache.get(tileId);
        sm.addGeometry(geometry);
        idCache.put(coords, tileId);
    }

    private void remove(PointDTO coords, Geometry geometry) {
        if (idCache.containsKey(coords)) {
            terrainCache.get(idCache.get(coords)).removeGeometry(geometry);
        }
    }

    private boolean containsType(String tileId) {
        return terrainCache.containsKey(tileId);
    }

    private SimpleModel cteateVisbileType(ImageData tile, String tileId) {
        Texture terrain = TextureLoader.loadTexture(tile);
        Appearance fog = new Appearance(terrain);
        fog.setColor(new Color(1.0, 0.8, 0.9));
        SimpleModel sm = new SimpleModel(fog);
        terrainCache.put(tileId, sm);
        sm.setId(tileId);
        return sm;
    }

    private SimpleModel createFogType(ImageData tile, String tileId) {
        Texture terrain = TextureLoader.loadTexture(tile);
        Appearance fog = new Appearance(terrain);
        fog.setColor(new Color(0.5, 0.4, 0.4));
        SimpleModel sm = new SimpleModel(fog);
        terrainCache.put(tileId, sm);
        sm.setId(tileId);
        return sm;
    }

    private SimpleModel createPoweredType(ImageData tile, String tileId) {
        Texture terrain = TextureLoader.loadTexture(tile);
        SimpleModel sm = new SimpleModel(new Appearance(terrain));
        sm.setId(tileId);
        terrainCache.put(tileId, sm);
        return sm;
    }
}
