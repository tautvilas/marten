package marten.aoe.gui.widget;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import marten.age.control.MouseListener;
import marten.age.graphics.appearance.Appearance;
import marten.age.graphics.appearance.Color;
import marten.age.graphics.flat.sprite.Sprite;
import marten.age.graphics.flat.sprite.TextureSprite;
import marten.age.graphics.geometry.Geometry;
import marten.age.graphics.geometry.primitives.Rectangle;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.model.ComplexModel;
import marten.age.graphics.model.SimpleModel;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import marten.age.graphics.texture.TextureLoader;
import marten.age.graphics.transform.TranslationGroup;
import marten.age.widget.Widget;
import marten.aoe.engine.Engine;
import marten.aoe.engine.Tile;
import marten.aoe.engine.TileCoordinate;

import org.apache.log4j.Logger;

public class MapWidget extends Sprite implements Widget, MouseListener {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(MapWidget.class);
    private HashMap<String, SimpleModel> terrainCache = new HashMap<String, SimpleModel>();
    private HashMap<Tile, Point> tiles = new HashMap<Tile, Point>();
    private BitmapFont font = FontCache.getFont(new Font("Courier New",
            Font.BOLD, 20));
    private TranslationGroup tg = new TranslationGroup();
    // private Tile activeTile = null;
    private int tileWidth;
    private int tileHeight;
    private Engine engine;
    private ComplexModel cm = new ComplexModel();
    private TextureSprite tileHighlight = null;

    public MapWidget(Engine engine, String mapName) {
        this.engine = engine;
        try {
            tileHighlight = new TextureSprite(new ImageData(
                    "data/gui/skin/tile-highlight.png"));
            tileHighlight.setPosition(new Point(-10000, -10000));
        } catch (IOException e1) {
            throw (new RuntimeException(e1));
        }
        // FIXME(carnifex) Is it really necessary to pass an argument into a
        // method for logging purposes only? :-/
        log.info("Loading map tiles for '" + mapName + "'...");
        Set<String> definedTerrain = engine.terrain.definedTerrain();
        for (String terrainType : definedTerrain) {
            try {
                log.info("Reading '" + terrainType + ".png'...");
                ImageData terrain = new ImageData("data/gui/tiles/"
                        + terrainType + ".png");
                this.tileWidth = terrain.width;
                this.tileHeight = terrain.height;
                if (!terrainCache.containsKey(terrainType)) {
                    SimpleModel simpleModel = new SimpleModel(new Appearance(
                            TextureLoader.loadTexture(terrain)));
                    terrainCache.put(terrainType, simpleModel);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("Loaded.");
        for (Tile tile : engine.tileMap.selectAll()) {
            Point tileCoordinates = getTileDisplayCoordinates(tile.at());
            Geometry geometry = new Rectangle(new Dimension(tileWidth,
                    tileHeight), tileCoordinates);
            SimpleModel sm = terrainCache.get(tile.terrain().name());
            sm.addGeometry(geometry);
            tiles.put(tile, tileCoordinates);
            // tg.addChild(tileWidget);
        }
        for (Tile tile : engine.tileMap.selectAll()) {
            BitmapString coords = new BitmapString(font, tile.at().x() + ":"
                    + tile.at().y(), new Color(0.0, 1.0, 0.0));
            coords.setPosition(tiles.get(tile));
            // tg.addChild(coords);
        }
        cm.setAppearance(new Appearance(new Color(1.0, 1.0, 1.0)));
        for (SimpleModel sm : terrainCache.values()) {
            cm.addPart(sm);
        }
        tg.addChild(cm);
        tg.addChild(tileHighlight);
        this.addChild(tg);
    }

    private Point getTileDisplayCoordinates(TileCoordinate position) {
        int delta = tileWidth + tileWidth / 2;
        if (position.x() % 2 == 0) {
            return new Point((position.x() / 2) * delta, position.y()
                    * tileHeight);
        } else {
            int deltax = tileHeight * 3 / 4;
            if (position.x() < 0) {
                deltax = -deltax;
            }
            return new Point((position.x() / 2) * delta + deltax, position.y()
                    * tileHeight - tileHeight / 2);
        }
    }

    private Tile getTile(Point coords, boolean odd)
            throws IndexOutOfBoundsException {
        if (Math.abs(coords.x % (tileWidth + tileWidth / 2)) <= tileWidth) {
            int tileX = ((int) coords.x / (tileWidth + tileWidth / 2)) * 2;
            int tileY = (int) coords.y / (tileHeight);
            if (odd && coords.x < 0)
                tileX -= 1;
            else if (odd)
                tileX += 1;
            Tile tile = this.engine.tileMap
                    .get(new TileCoordinate(tileX, tileY));
            return tile;
        }
        throw new IndexOutOfBoundsException("Tile index is out of bounds");
    }

    private Tile tileHit(Point mouseCoords) {
        // Substract map translation form mouse coordinates
        mouseCoords.x -= this.getPosition().x;
        mouseCoords.y -= this.getPosition().y;
        // Create a copy of mouse coordinates for modification
        Point coords = new Point(mouseCoords);
        // Tile hit candidate array
        ArrayList<Tile> candidates = new ArrayList<Tile>();

        // Hack for dealing with negative coordinates
        if (coords.y < 0)
            coords.y -= tileWidth;
        if (coords.x < 0)
            coords.x -= tileWidth;

        // Check hit for even tile columns
        try {
            Tile tile = getTile(coords, false);
            candidates.add(tile);
        } catch (IndexOutOfBoundsException e) {
        }

        // check hit for odd tile columns
        if (coords.x < 0)
            coords.x += tileWidth * 3 / 4;
        else
            coords.x -= tileWidth * 3 / 4;
        coords.y += tileHeight / 2;
        try {
            Tile tile = getTile(coords, true);
            candidates.add(tile);
        } catch (IndexOutOfBoundsException e) {
        }

        // find a candidate with the closest distance to tile center
        if (candidates.isEmpty()) {
            return null;
        } else if (candidates.size() == 1) {
            return candidates.get(0);
        } else {
            double mindistance = tileWidth * 2;
            Tile result = null;
            for (Tile tile : candidates) {
                Point facePosition = tiles.get(tile);
                Point faceCenter = new Point(facePosition.x + tileWidth / 2,
                        facePosition.y + tileHeight / 2);
                double dx = Math.abs(faceCenter.x - mouseCoords.x);
                double dy = Math.abs(faceCenter.y - mouseCoords.y);
                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance < mindistance) {
                    mindistance = distance;
                    result = tile;
                }
            }
            return result;
        }
    }

    @Override
    public int getHeight() {
        return (this.engine.tileMap.maxY() - this.engine.tileMap.minY() + 1)
                * (this.tileHeight) + this.tileHeight / 2;
    }

    @Override
    public Point getPosition() {
        return tg.getCoordinates();
    }

    @Override
    public int getWidth() {
        return (this.engine.tileMap.maxX() - this.engine.tileMap.minX() + 1)
                * (this.tileWidth * 3 / 4) + this.tileWidth / 4;
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
        Tile tile = tileHit(coords);
        if (tile != null) {
            tileHighlight.setPosition(tiles.get(tile));
        }
    }

    @Override
    public void mouseUp(Point coords) {
    }

    @Override
    public void mouseWheelRoll(int delta) {
    }
}
