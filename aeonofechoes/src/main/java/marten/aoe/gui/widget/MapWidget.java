package marten.aoe.gui.widget;

import java.awt.Font;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import marten.age.graphics.BasicSceneGraphBranch;
import marten.age.graphics.flat.sprite.TextureSprite;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import marten.age.widget.Widget;
import marten.aoe.engine.TerrainDatabase;
import marten.aoe.engine.Tile;
import marten.aoe.engine.TileCoordinate;
import marten.aoe.engine.TileMap;

import org.apache.log4j.Logger;

public class MapWidget extends BasicSceneGraphBranch implements Widget {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(MapWidget.class);
    private HashMap<String, ImageData> terrainCache = new HashMap<String, ImageData>();
    private BitmapFont font = FontCache.getFont(new Font("Courier New",
            Font.BOLD, 20));

    public MapWidget(String mapName) {
        log.info("Loading map tiles for '" + mapName + "'...");
        Set<String> definedTerrain = TerrainDatabase.definedTerrain();
        for (String terrainType : definedTerrain) {
            try {
                log.info("Reading '" + terrainType + ".png'...");
                ImageData terrain = new ImageData("data/gui/tiles/"
                        + terrainType + ".png");
                terrainCache.put(terrainType, terrain);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("Loaded.");
        for (Tile tile : TileMap.selectAll()) {
            TileFace tileWidget = new TileFace(terrainCache.get(tile
                    .terrain().name()), tile.at());
            this.addChild(tileWidget);
        }
        for (Tile tile : TileMap.selectAll()) {
            TileFace tileWidget = new TileFace(terrainCache.get(tile
                    .terrain().name()), tile.at());
            BitmapString coords = new BitmapString(font, tile.at().x() + ""
                    + tile.at().y());
            coords.setPosition(tileWidget.getPosition());
            this.addChild(coords);
        }
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
                        position.y() * imageHeight + imageHeight / 2));
            }
        }
    }
}
