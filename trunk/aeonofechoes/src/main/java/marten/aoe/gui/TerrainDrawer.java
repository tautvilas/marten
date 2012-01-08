package marten.aoe.gui;

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
import marten.age.graphics.primitives.TextureCoords;
import marten.age.graphics.texture.Texture;
import marten.age.graphics.texture.TextureLoader;
import marten.aoe.dto.TileDTO;

public class TerrainDrawer {

    private SceneGraphBranch<SceneGraphChild> context;

    public TerrainDrawer(SceneGraphBranch<SceneGraphChild> context) {
        this.context = context;
    }

    private final HashMap<String, SimpleModel> terrainCache = new HashMap<String, SimpleModel>();

    public void updateTile(TileDTO tile, TileDTO oldTile, Point displayCoords, TileDTO[] surrounds) {
        String tileId = TileImageFactory.getTileGuiId(tile);
        if (!this.containsType(tileId)) {
            if (tile.getVisibility()) {
                this.context.addChild(this.cteateVisbileType(tile));
            } else {
                this.context.addChild(this.createFogType(tile));
            }
        }
        TextureCoords coords = this.terrainCache.get(tileId).getAppearance().getTexture().getCoords();
        Rectangle geometry = new Rectangle(displayCoords, new Dimension(84, 72), coords);
        if (oldTile != null) {
            String oldTileId = TileImageFactory.getTileGuiId(oldTile);
            this.remove(oldTileId, oldTile.getVisibility(), geometry);
        }
        this.put(tileId, tile.getVisibility(), geometry);
    }

    private void put(String tileId, boolean visible, Geometry geometry) {
        terrainCache.get(tileId).addGeometry(geometry);
    }

    private void remove(String tileId, boolean visible, Geometry geometry) {
        terrainCache.get(tileId).removeGeometry(geometry);
    }

    private boolean containsType(String tileId) {
        return terrainCache.containsKey(tileId);
    }

    private SimpleModel cteateVisbileType(TileDTO tile) {
        String tileId = TileImageFactory.getTileGuiId(tile);
        Texture terrain = TextureLoader.loadTexture(TileImageFactory
                .getTile(tile));
        SimpleModel sm = new SimpleModel(new Appearance(terrain));
        sm.setId(tileId);
        terrainCache.put(tileId, sm);
        return sm;
    }

    private SimpleModel createFogType(TileDTO tile) {
        String tileId = TileImageFactory.getTileGuiId(tile);
        Texture terrain = TextureLoader.loadTexture(TileImageFactory
                .getTile(tile));
        Appearance fog = new Appearance(terrain);
        fog.setColor(new Color(0.5, 0.4, 0.4));
        SimpleModel sm = new SimpleModel(fog);
        terrainCache.put(tileId, sm);
        sm.setId(tileId);
        return sm;
    }
}
