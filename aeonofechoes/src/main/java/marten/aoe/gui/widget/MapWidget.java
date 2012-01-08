package marten.aoe.gui.widget;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import marten.age.control.MouseListener;
import marten.age.graphics.BasicSceneGraphBranch;
import marten.age.graphics.SceneGraphBranch;
import marten.age.graphics.SceneGraphChild;
import marten.age.graphics.appearance.Color;
import marten.age.graphics.flat.sprite.TextureSprite;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.layout.BoxedObject;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import marten.age.graphics.transform.TranslationGroup;
import marten.aoe.dto.MapDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.gui.MapWidgetListener;
import marten.aoe.gui.TerrainDrawer;
import marten.aoe.gui.UnitDrawer;

import org.apache.log4j.Logger;

public class MapWidget extends BasicSceneGraphBranch<SceneGraphChild> implements
        MouseListener, BoxedObject {
    @SuppressWarnings("unused")
    private static org.apache.log4j.Logger log = Logger
            .getLogger(MapWidget.class);

    private final int TILE_WIDTH = 84;
    private final int TILE_HEIGHT = 72;

    private PointDTO selectedTile = null;
    private MapWidgetListener listener;

    private final BitmapFont font = FontCache.getFont(new Font("Courier New",
            Font.BOLD, 20));
    private final TranslationGroup tg = new TranslationGroup();
    private HashMap<PointDTO, TileDTO> tiles = new HashMap<PointDTO, TileDTO>();
    private TextureSprite tileHighlight = null;
    private TextureSprite tileSelection = null;
    private Dimension dimension;
    private Dimension size;

    private TerrainDrawer terrainDrawer;
    private UnitDrawer unitDrawer = new UnitDrawer(this.tg);

    private void init() {
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
        SceneGraphBranch<SceneGraphChild> terrain = new BasicSceneGraphBranch<SceneGraphChild>();
        tg.addChild(terrain);
        this.terrainDrawer = new TerrainDrawer(terrain);
        tg.addChild(tileHighlight);
        this.addChild(tg);
        this.setPosition(this.getPosition().move(new Point(0, 0)));
    }

    public MapWidget(int size, Dimension dimension) {
        this.dimension = dimension;
        this.size = new Dimension(size, size);
        this.init();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.updateTile(new TileDTO("Grassland", new PointDTO(i, j),
                        null, true));
            }
        }
    }

    public MapWidget(MapDTO map, Dimension dimension) {
        this(map, dimension, null);
    }

    public MapWidget(MapDTO map, Dimension dimension, MapWidgetListener listener) {
        this.listener = listener;
        this.dimension = dimension;
        this.size = new Dimension(map.getMeta().getWidth(), map.getMeta()
                .getHeight());
        this.init();
        for (TileDTO[] tileLine : map.getTileMap()) {
            for (TileDTO tile : tileLine) {
                this.updateTile(tile);
            }
        }
        for (TileDTO[] tileLine : map.getTileMap()) {
            for (TileDTO tile : tileLine) {
                BitmapString coords = new BitmapString(font, tile
                        .getCoordinates().getX()
                        + ":"
                        + tile.getCoordinates().getY(),
                        new Color(0.0, 1.0, 0.0));
                coords.setPosition(this.getTileDisplayCoordinates(tile
                        .getCoordinates()));
                // tg.addChild(coords);
            }
        }
    }

    private Point getTileDisplayCoordinates(PointDTO position) {
        int delta = TILE_WIDTH + TILE_WIDTH / 2;
        if (position.getX() % 2 == 0) {
            return new Point((position.getX() / 2) * delta, position.getY()
                    * TILE_HEIGHT);
        } else {
            int deltax = TILE_WIDTH * 3 / 4;
            if (position.getX() < 0) {
                deltax = -deltax;
            }
            return new Point((position.getX() / 2) * delta + deltax,
                    position.getY() * TILE_HEIGHT + TILE_HEIGHT / 2);
        }
    }

    private TileDTO getTile(Point coords, boolean even)
            throws IndexOutOfBoundsException {
        PointDTO position = null;
        int mx = (int)coords.x;
        int my = (int)coords.y;
        int tileX;
        int tileY;
        if (even) {
            tileX = mx / (TILE_WIDTH + TILE_WIDTH / 2) * 2;
            tileY = my / TILE_HEIGHT;
        } else {
            tileX = (mx - TILE_WIDTH / 4) / (TILE_WIDTH + TILE_WIDTH / 2) * 2
                    + 1;
            tileY = (my - TILE_HEIGHT / 2) / TILE_HEIGHT;
        }
        position = new PointDTO(tileX, tileY);
        if (tiles.containsKey(position)) {
            return this.tiles.get(position);
        } else {
            throw new IndexOutOfBoundsException("Tile index is out of bounds");
        }
    }

    public TileDTO tileHit(Point position) {
        if (position.x > dimension.width || position.y > dimension.height) {
            return null;
        }
        // Substract map translation form mouse coordinates
        Point coords = position.substract(this.getPosition());
        // Tile hit candidate array
        ArrayList<TileDTO> candidates = new ArrayList<TileDTO>();

        // Check hit for even tile columns
        try {
            TileDTO tile = getTile(coords, true);
            candidates.add(tile);
        } catch (IndexOutOfBoundsException e) {
        }
        // Check hit for odd tile columns
        try {
            TileDTO tile = getTile(coords, false);
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
                double dx = Math.abs(faceCenter.x - coords.x);
                double dy = Math.abs(faceCenter.y - coords.y);
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
        Point tileDisplayCoordinates = this.getTileDisplayCoordinates(tile
                .getCoordinates());
        PointDTO tileCoordinates = tile.getCoordinates();
        TileDTO surrounds[] = this.getSurrounds(tileCoordinates);
        this.terrainDrawer.updateTile(tile, tiles.get(tileCoordinates),
                tileDisplayCoordinates, surrounds);
        for (int i = 0; i < surrounds.length; i++) {
            TileDTO next = surrounds[i];
            if (next == null) continue;
            PointDTO nextCoords = next.getCoordinates();
            this.terrainDrawer.updateTile(surrounds[i], tiles.get(nextCoords),
                    this.getTileDisplayCoordinates(nextCoords),
                    this.getSurrounds(nextCoords));
        }
        this.tiles.put(tileCoordinates, tile);
        this.unitDrawer.updateTile(tile, tileDisplayCoordinates);
    }

    private TileDTO[] getSurrounds(PointDTO coords) {
        TileDTO[] surrounds = new TileDTO[6];
        int x = coords.getX();
        int y = coords.getY();
        surrounds[0] = this.tiles.get(new PointDTO(x, y - 1)); // s
        surrounds[3] = this.tiles.get(new PointDTO(x, y + 1)); // n
        if (x % 2 == 0) {
            surrounds[1] = this.tiles.get(new PointDTO(x - 1, y - 1)); // sw
            surrounds[2] = this.tiles.get(new PointDTO(x - 1, y)); // nw
            surrounds[4] = this.tiles.get(new PointDTO(x + 1, y)); // ne
            surrounds[5] = this.tiles.get(new PointDTO(x + 1, y - 1)); // se
        } else {
            surrounds[1] = this.tiles.get(new PointDTO(x - 1, y)); // sw
            surrounds[2] = this.tiles.get(new PointDTO(x - 1, y + 1)); // nw
            surrounds[4] = this.tiles.get(new PointDTO(x + 1, y + 1)); // ne
            surrounds[5] = this.tiles.get(new PointDTO(x + 1, y)); // se
        }
        return surrounds;
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public Dimension getDimension() {
        return this.size;
    }

    public Dimension getSize() {
        return this.size;
    }

    @Override
    public Point getPosition() {
        return tg.getPosition();
    }

    public Collection<TileDTO> getTiles() {
        return this.tiles.values();
    }

    @Override
    public void setPosition(Point position) {
        int maxX = (int)this.size.width * (this.TILE_WIDTH * 3 / 4)
                + this.TILE_WIDTH / 4;
        int maxY = (int)this.size.height * this.TILE_HEIGHT + this.TILE_HEIGHT
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

    public TileDTO getTile(PointDTO point) {
        return this.tiles.get(point);
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
                TileDTO oldTile = this.tiles.get(this.selectedTile);
                if (oldTile.getUnit() != null) {
                    this.listener.moveUnit(this.selectedTile,
                            tile.getCoordinates());
                    this.selectedTile = null;
                    return;
                }
            }
            this.selectedTile = tile.getCoordinates();
        }
    }

    @Override
    public void mouseWheelRoll(int delta, Point coords) {
    }
}
