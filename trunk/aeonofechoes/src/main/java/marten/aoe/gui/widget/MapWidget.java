package marten.aoe.gui.widget;

import java.awt.Font;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import marten.age.control.MouseListener;
import marten.age.graphics.appearance.Color;
import marten.age.graphics.flat.sprite.Sprite;
import marten.age.graphics.flat.sprite.TextureSprite;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import marten.age.graphics.transform.TranslationGroup;
import marten.age.widget.Widget;
import marten.aoe.engine.TerrainDatabase;
import marten.aoe.engine.Tile;
import marten.aoe.engine.TileCoordinate;
import marten.aoe.engine.TileMap;

import org.apache.log4j.Logger;

public class MapWidget extends Sprite implements Widget, MouseListener {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(MapWidget.class);
    private HashMap<String, ImageData> terrainCache = new HashMap<String, ImageData>();
    private HashMap<Tile, TileFace> tiles = new HashMap<Tile, TileFace>();
    private BitmapFont font = FontCache.getFont(new Font("Courier New",
            Font.BOLD, 20));
    private TranslationGroup tg = new TranslationGroup();
    private int tileWidth;
    private int tileHeight;

    public MapWidget(String mapName) {
        log.info("Loading map tiles for '" + mapName + "'...");
        Set<String> definedTerrain = TerrainDatabase.definedTerrain();
        for (String terrainType : definedTerrain) {
            try {
                log.info("Reading '" + terrainType + ".png'...");
                ImageData terrain = new ImageData("data/gui/tiles/"
                        + terrainType + ".png");
                this.tileWidth = terrain.width;
                this.tileHeight = terrain.height;
                terrainCache.put(terrainType, terrain);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("Loaded.");
        for (Tile tile : TileMap.selectAll()) {
            TileFace tileWidget = new TileFace(terrainCache.get(tile.terrain()
                    .name()), tile.at());
            tiles.put(tile, tileWidget);
            tg.addChild(tileWidget);
        }
        for (Tile tile : TileMap.selectAll()) {
            BitmapString coords = new BitmapString(font, tile.at().x() + ""
                    + tile.at().y());
            coords.setPosition(tiles.get(tile).getPosition());
            tg.addChild(coords);
        }
        this.addChild(tg);
    }

    private class TileFace extends TextureSprite {
        public TileFace(ImageData data, TileCoordinate position) {
            super(data);
            int imageWidth = data.width;
            int imageHeight = data.height;
            int delta = imageWidth + imageWidth / 2;
            if (position.x() % 2 == 0) {
                this.setPosition(new Point((position.x() / 2) * delta, position
                        .y()
                        * imageHeight));
            } else {
                int deltax = imageHeight * 3 / 4;
                if (position.x() < 0) {
                    deltax = -deltax;
                }
                this.setPosition(new Point((position.x() / 2) * delta + deltax,
                        position.y() * imageHeight - imageHeight / 2));
            }
            this.setColor(new Color(0.5, 0.5, 1));
        }
    }

    private TileFace tileHit(Point coords) {
        return null;
    }

    @Override
    public int getHeight() {
        return (TileMap.maxY() - TileMap.minY() + 1) * (this.tileHeight)
                + this.tileHeight / 2;
    }

    @Override
    public Point getPosition() {
        return tg.getCoordinates();
    }

    @Override
    public int getWidth() {
        return (TileMap.maxX() - TileMap.minX() + 1) * (this.tileWidth * 3 / 4)
                + this.tileWidth / 4;
    }

    @Override
    public void setPosition(Point position) {
        tg.setCoordinates(position);
    }

    @Override
    public void mouseDown(Point coords) {
    }

    @Override
    public void mouseMove(Point coords) {
        TileFace tile = tileHit(coords);
        if (tileHit(coords) != null) {
            System.out.println(tile);
        }
    }

    @Override
    public void mouseUp(Point coords) {
    }

    @Override
    public void mouseWheelRoll(int delta) {
    }
}
