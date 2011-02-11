package marten.aoe.gui.widget;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import marten.age.control.MouseListener;
import marten.age.graphics.BasicSceneGraphBranch;
import marten.age.graphics.appearance.Appearance;
import marten.age.graphics.appearance.Color;
import marten.age.graphics.flat.sprite.TextureSprite;
import marten.age.graphics.geometry.Geometry;
import marten.age.graphics.geometry.primitives.Rectangle;
import marten.age.graphics.image.ImageCache;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.layout.BoxedObject;
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
import marten.aoe.Path;
import marten.aoe.dto.MapDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.gui.MapWidgetListener;

import org.apache.log4j.Logger;

public class MapWidget extends BasicSceneGraphBranch implements Widget,
        MouseListener, BoxedObject {
    @SuppressWarnings("unused")
    private static org.apache.log4j.Logger log = Logger
            .getLogger(MapWidget.class);

    private final int TILE_WIDTH = 64;
    private final int TILE_HEIGHT = 64;

    private final HashMap<String, SimpleModel> terrainCache = new HashMap<String, SimpleModel>();
    private MapDTO map = null;
    private PointDTO selectedTile = null;
    private MapWidgetListener listener;

    private final BitmapFont font = FontCache.getFont(new Font("Courier New",
            Font.BOLD, 20));
    private final TranslationGroup tg = new TranslationGroup();
    private final ComplexModel cm = new ComplexModel();
    private TextureSprite tileHighlight = null;
    private TextureSprite tileSelection = null;
    private Dimension dimension;
    private volatile LinkedList<TileDTO> updatedTiles = new LinkedList<TileDTO>();

    public MapWidget(MapDTO map, Dimension dimension, MapWidgetListener listener) {
        this.listener = listener;
        this.dimension = dimension;
        this.map = map;
        try {
            this.tileHighlight = new TextureSprite(new ImageData(
                    "data/gui/skin/tile-highlight.png"));
            this.tileSelection = new TextureSprite(new ImageData(
                    "data/gui/skin/tile-highlight.png"));
            this.tileSelection.setColor(new Color(1.0, 0, 0));
            tileHighlight.setPosition(new Point(-10000, -10000));
        } catch (IOException e1) {
            throw (new RuntimeException(e1));
        }
        for (TileDTO[] tileLine : this.map.getTileMap()) {
            for (TileDTO tile : tileLine) {
                this.updateTile(tile);
            }
        }
        cm.setAppearance(new Appearance(new Color(1.0, 1.0, 1.0)));
        for (SimpleModel sm : terrainCache.values()) {
            cm.addPart(sm);
        }
        tg.addChild(cm);
        tg.addChild(tileHighlight);
        for (TileDTO[] tileLine : map.getTileMap()) {
            for (TileDTO tile : tileLine) {
                BitmapString coords = new BitmapString(font, tile
                        .getCoordinates().getX()
                        + ":" + tile.getCoordinates().getY(), new Color(0.0,
                        1.0, 0.0));
                coords.setPosition(this.getTileDisplayCoordinates(tile
                        .getCoordinates()));
                 tg.addChild(coords);
            }
        }
        this.addChild(tg);
        this.setPosition(this.getPosition().move(new Point(0, 0)));
    }

    private Point getTileDisplayCoordinates(PointDTO position) {
        int delta = TILE_WIDTH + TILE_WIDTH / 2;
        if (position.getX() % 2 == 0) {
            return new Point((position.getX() / 2) * delta, position.getY()
                    * TILE_HEIGHT);
        } else {
            int deltax = TILE_HEIGHT * 3 / 4;
            if (position.getX() < 0) {
                deltax = -deltax;
            }
            return new Point((position.getX() / 2) * delta + deltax, position
                    .getY()
                    * TILE_HEIGHT + TILE_HEIGHT / 2);
        }
    }

    private TileDTO getTile(Point coords, boolean odd)
            throws IndexOutOfBoundsException {
        if (Math.abs(coords.x % (TILE_WIDTH + TILE_WIDTH / 2)) <= TILE_WIDTH) {
            int tileX = ((int)coords.x / (TILE_WIDTH + TILE_WIDTH / 2)) * 2;
            int tileY = (int)coords.y / (TILE_HEIGHT);
            if (odd && coords.x < 0)
                tileX -= 1;
            else if (odd)
                tileX += 1;
            return this.map.getTileMap()[tileX][tileY];
        }
        throw new IndexOutOfBoundsException("Tile index is out of bounds");
    }

    private TileDTO tileHit(Point position) {
        // Substract map translation form mouse coordinates
        Point mouseCoords = new Point(position);
        mouseCoords.x -= this.getPosition().x;
        mouseCoords.y -= this.getPosition().y;
        // Create a copy of mouse coordinates for modification
        Point coords = new Point(mouseCoords);
        // Tile hit candidate array
        ArrayList<TileDTO> candidates = new ArrayList<TileDTO>();

        // Hack for dealing with negative coordinates
        if (coords.y < 0)
            coords.y -= TILE_WIDTH;
        if (coords.x < 0)
            coords.x -= TILE_WIDTH;

        // Check hit for even tile columns
        try {
            TileDTO tile = getTile(coords, false);
            candidates.add(tile);
        } catch (IndexOutOfBoundsException e) {
        }

        // check hit for odd tile columns
        if (coords.x < 0)
            coords.x += TILE_WIDTH * 3 / 4;
        else
            coords.x -= TILE_WIDTH * 3 / 4;
        coords.y -= TILE_HEIGHT / 2;
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
            double mindistance = TILE_WIDTH * 2;
            TileDTO result = null;
            for (TileDTO tile : candidates) {
                Point facePosition = this.getTileDisplayCoordinates(tile
                        .getCoordinates());
                Point faceCenter = new Point(facePosition.x + TILE_WIDTH / 2,
                        facePosition.y + TILE_HEIGHT / 2);
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

    public void updateTile(TileDTO tile) {
        this.updatedTiles.add(tile);
    }

    @Override
    public void render() {
        while (!this.updatedTiles.isEmpty()) {
            TileDTO tile = this.updatedTiles.pop();
            Point tileDisplayCoordinates = this.getTileDisplayCoordinates(tile
                    .getCoordinates());
            PointDTO tileCoordinates = tile.getCoordinates();
            // TileDTO oldTile =
            // this.map.getTileMap()[tileCoordinates.getX()][tileCoordinates
            // .getY()];
            Geometry geometry = new Rectangle(new Dimension(TILE_WIDTH,
                    TILE_HEIGHT), tileDisplayCoordinates);
            if (!this.terrainCache.containsKey(tile.getName())) {
                Texture terrain = TextureLoader.loadTexture(ImageCache
                        .getImage(Path.TILE_DATA_PATH
                                + tile.getName().toLowerCase()));
                SimpleModel sm = new SimpleModel(new Appearance(terrain));
                terrainCache.put(tile.getName(), sm);
                this.cm.addPart(sm);
            }
            // remove old tile graphic
            this.terrainCache
                    .get(
                            this.map.getTileMap()[tileCoordinates.getX()][tileCoordinates
                                    .getY()].getName())
                    .removeGeometry(geometry);
            SimpleModel sm = terrainCache.get(tile.getName());
            sm.addGeometry(geometry);
            this.map.getTileMap()[tileCoordinates.getX()][tileCoordinates
                    .getY()] = tile;
            if (tile.getUnit() != null) {
                BitmapString unit = new BitmapString(font, tile.getUnit()
                        .getName().charAt(0)
                        + "", new Color(0.0, 1.0, 0.0));
                unit.setPosition(new Point(tileDisplayCoordinates.x
                        + this.TILE_WIDTH / 2 - unit.getDimension().width / 2,
                        tileDisplayCoordinates.y + this.TILE_HEIGHT / 2
                                - unit.getDimension().height / 2));
                this.tg.addChild(unit);
            }
        }
        super.render();
    }

    @Override
    public Dimension getDimension() {
        return new Dimension(this.map.getWidth(), this.map.getHeight());
    }

    @Override
    public Point getPosition() {
        return tg.getPosition();
    }

    @Override
    public void setPosition(Point position) {
        int maxX = this.map.getWidth() * (this.TILE_WIDTH * 3 / 4)
                + this.TILE_WIDTH / 4;
        int maxY = this.map.getHeight() * this.TILE_HEIGHT + this.TILE_HEIGHT
                / 2;
        Point currentPosition = this.getPosition();
        if (currentPosition.x > 0 && position.x > currentPosition.x) {
            position.x = currentPosition.x;
        } else if (currentPosition.x + maxX < this.dimension.width
                && position.x < currentPosition.x) {
            position.x = currentPosition.x;
        }
        if (currentPosition.y > 0 && position.y > currentPosition.y) {
            position.y = currentPosition.y;
        } else if (currentPosition.y + maxY < this.dimension.height
                && position.y < currentPosition.y) {
            position.y = currentPosition.y;
        }
        tg.setPosition(position);
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
    public void mouseDown(Point coords) {
    }

    @Override
    public void mouseUp(Point coords) {
        TileDTO tile = tileHit(coords);
        if (tile != null) {
            this.tileSelection.setPosition(this.getTileDisplayCoordinates(tile
                    .getCoordinates()));
            if (this.selectedTile == null) {
                tg.addChild(tileSelection);
            } else {
                TileDTO oldTile = this.map.getTileDTO(this.selectedTile);
                if (oldTile.getUnit() != null) {
                    this.listener.moveUnit(this.selectedTile, tile.getCoordinates());
                }
            }
            this.selectedTile = tile.getCoordinates();
        }
    }

    @Override
    public void mouseWheelRoll(int delta, Point coords) {
    }
}
