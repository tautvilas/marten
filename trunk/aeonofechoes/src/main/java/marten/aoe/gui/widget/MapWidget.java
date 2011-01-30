package marten.aoe.gui.widget;

import java.awt.Font;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import marten.age.control.MouseListener;
import marten.age.core.AppInfo;
import marten.age.graphics.appearance.Appearance;
import marten.age.graphics.appearance.Color;
import marten.age.graphics.flat.sprite.BasicSprite;
import marten.age.graphics.flat.sprite.TextureSprite;
import marten.age.graphics.geometry.Geometry;
import marten.age.graphics.geometry.primitives.Rectangle;
import marten.age.graphics.image.ImageCache;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.model.ComplexModel;
import marten.age.graphics.model.SimpleModel;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import marten.age.graphics.texture.Texture;
import marten.age.graphics.texture.TextureLoader;
import marten.age.graphics.transform.TranslationGroup;
import marten.age.widget.Widget;
import marten.aoe.dto.MapDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.server.face.EngineFace;

import org.apache.log4j.Logger;

public class MapWidget extends BasicSprite implements Widget, MouseListener {
    @SuppressWarnings("unused")
    private static org.apache.log4j.Logger log = Logger
            .getLogger(MapWidget.class);
    private final HashMap<String, SimpleModel> terrainCache = new HashMap<String, SimpleModel>();
    private TileDTO[][] map = null;
    private final BitmapFont font = FontCache.getFont(new Font("Courier New",
            Font.BOLD, 20));
    private final TranslationGroup tg = new TranslationGroup();
    // private Tile activeTile = null;
    private final int tileWidth = 64;
    private final int tileHeight = 64;
    private final EngineFace engine;
    private final ComplexModel cm = new ComplexModel();
    private TextureSprite tileHighlight = null;

    private int minX = Integer.MAX_VALUE;
    private int maxX = Integer.MIN_VALUE;
    private int minY = Integer.MAX_VALUE;
    private int maxY = Integer.MIN_VALUE;

    public MapWidget(EngineFace engine) {
        this.engine = engine;
        try {
            tileHighlight = new TextureSprite(new ImageData(
                    "data/gui/skin/tile-highlight.png"));
            tileHighlight.setPosition(new Point(-10000, -10000));
        } catch (IOException e1) {
            throw (new RuntimeException(e1));
        }
        try {
            this.map = this.engine.getMap().getTileMap();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        for (TileDTO[] tileLine : map) {
            for (TileDTO tile : tileLine) {
                Point tileCoordinates = this.getTileDisplayCoordinates(tile
                        .getCoordinates());
                updateDimensionConstraints(tileCoordinates);
                Geometry geometry = new Rectangle(new Dimension(tileWidth,
                        tileHeight), tileCoordinates);
                if (!this.terrainCache.containsKey(tile.getName())) {
                    Texture terrain = TextureLoader.loadTexture(ImageCache
                            .getImage("data/gui/tiles/"
                                    + tile.getName().toLowerCase() + ".png"));
                    terrainCache.put(tile.getName(), new SimpleModel(
                            new Appearance(terrain)));
                }
                SimpleModel sm = terrainCache.get(tile.getName());
                sm.addGeometry(geometry);
            }
        }
        cm.setAppearance(new Appearance(new Color(1.0, 1.0, 1.0)));
        for (SimpleModel sm : terrainCache.values()) {
            cm.addPart(sm);
        }
        tg.addChild(cm);
        tg.addChild(tileHighlight);
        for (TileDTO[] tileLine : map) {
            for (TileDTO tile : tileLine) {
                BitmapString coords = new BitmapString(font, tile
                        .getCoordinates().getX()
                        + ":" + tile.getCoordinates().getY(), new Color(0.0,
                        1.0, 0.0));
                coords.setPosition(this.getTileDisplayCoordinates(tile
                        .getCoordinates()));
                // tg.addChild(coords);
            }
        }
        this.addChild(tg);
        this.setPosition(this.getPosition().move(new Point(-minX, -minY)));
    }

    private void updateDimensionConstraints(Point tileCoordinates) {
        if (tileCoordinates.x + tileWidth > this.maxX)
            this.maxX = (int) tileCoordinates.x + tileWidth;
        else if (tileCoordinates.x < this.minX)
            this.minX = (int) tileCoordinates.x;
        if (tileCoordinates.y + tileHeight > this.maxY)
            this.maxY = (int) tileCoordinates.y + tileHeight;
        else if (tileCoordinates.y < this.minY)
            this.minY = (int) tileCoordinates.y;
    }

    private Point getTileDisplayCoordinates(PointDTO position) {
        int delta = tileWidth + tileWidth / 2;
        if (position.getX() % 2 == 0) {
            return new Point((position.getX() / 2) * delta, position.getY()
                    * tileHeight);
        } else {
            int deltax = tileHeight * 3 / 4;
            if (position.getX() < 0) {
                deltax = -deltax;
            }
            return new Point((position.getX() / 2) * delta + deltax, position
                    .getY()
                    * tileHeight + tileHeight / 2);
        }
    }

    private TileDTO getTile(Point coords, boolean odd)
            throws IndexOutOfBoundsException {
        if (Math.abs(coords.x % (tileWidth + tileWidth / 2)) <= tileWidth) {
            int tileX = ((int) coords.x / (tileWidth + tileWidth / 2)) * 2;
            int tileY = (int) coords.y / (tileHeight);
            if (odd && coords.x < 0)
                tileX -= 1;
            else if (odd)
                tileX += 1;
            return map[tileX][tileY];
        }
        throw new IndexOutOfBoundsException("Tile index is out of bounds");
    }

    private TileDTO tileHit(Point mouseCoords) {
        // Substract map translation form mouse coordinates
        mouseCoords.x -= this.getPosition().x;
        mouseCoords.y -= this.getPosition().y;
        // Create a copy of mouse coordinates for modification
        Point coords = new Point(mouseCoords);
        // Tile hit candidate array
        ArrayList<TileDTO> candidates = new ArrayList<TileDTO>();

        // Hack for dealing with negative coordinates
        if (coords.y < 0)
            coords.y -= tileWidth;
        if (coords.x < 0)
            coords.x -= tileWidth;

        // Check hit for even tile columns
        try {
            TileDTO tile = getTile(coords, false);
            candidates.add(tile);
        } catch (IndexOutOfBoundsException e) {
        }

        // check hit for odd tile columns
        if (coords.x < 0)
            coords.x += tileWidth * 3 / 4;
        else
            coords.x -= tileWidth * 3 / 4;
        coords.y -= tileHeight / 2;
        try {
            TileDTO tile = getTile(coords, true);
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
            TileDTO result = null;
            for (TileDTO tile : candidates) {
                Point facePosition = this.getTileDisplayCoordinates(tile
                        .getCoordinates());
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

    public void ScrollDown(int numPixels) {
        this.setPosition(new Point(this.getPosition().x, this.getPosition().y
                + numPixels));
    }

    public void ScrollUp(int numPixels) {
        this.setPosition(new Point(this.getPosition().x, this.getPosition().y
                - numPixels));
    }

    public void ScrollLeft(int numPixels) {
        this.setPosition(new Point(this.getPosition().x + numPixels, this
                .getPosition().y));
    }

    public void ScrollRight(int numPixels) {
        this.setPosition(new Point(this.getPosition().x - numPixels, this
                .getPosition().y));
    }

    @Override
    public Dimension getDimension() {
        // int width = (this.engine.tileMap.maxX() - this.engine.tileMap.minX()
        // + 1)
        // * (this.tileWidth * 3 / 4) + this.tileWidth / 4;
        // int height = (this.engine.tileMap.maxY() - this.engine.tileMap.minY()
        // + 1)
        // * (this.tileHeight) + this.tileHeight / 2;
        MapDTO map;
        try {
            map = this.engine.getMap();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return new Dimension(map.getWidth(), map.getHeight());
    }

    @Override
    public Point getPosition() {
        return tg.getPosition();
    }

    @Override
    public void setPosition(Point position) {
        Point currentPosition = this.getPosition();
        if (currentPosition.x + minX > 0 && position.x > currentPosition.x) {
            position.x = currentPosition.x;
        } else if (currentPosition.x + maxX < AppInfo.getDisplayWidth()
                && position.x < currentPosition.x) {
            position.x = currentPosition.x;
        }
        if (currentPosition.y + minY > 0 && position.y > currentPosition.y) {
            position.y = currentPosition.y;
        } else if (currentPosition.y + maxY < AppInfo.getDisplayHeight()
                && position.y < currentPosition.y) {
            position.y = currentPosition.y;
        }
        tg.setPosition(position);
    }

    @Override
    public void mouseDown(Point coords) {
    }

    @Override
    public void mouseMove(Point coords) {
        TileDTO tile = tileHit(coords);
        if (tile != null) {
            tileHighlight.setPosition(this.getTileDisplayCoordinates(tile
                    .getCoordinates()));
        }
    }

    @Override
    public void mouseUp(Point coords) {
    }

    @Override
    public void mouseWheelRoll(int delta, Point coords) {
    }
}
