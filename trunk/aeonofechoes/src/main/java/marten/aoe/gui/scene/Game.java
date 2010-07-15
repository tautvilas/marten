package marten.aoe.gui.scene;

import java.awt.Font;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import marten.age.control.KeyboardController;
import marten.age.control.KeyboardListener;
import marten.age.core.AgeScene;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import marten.aoe.engine.TerrainDatabase;
import marten.aoe.engine.Tile;
import marten.aoe.engine.TileMap;
import marten.aoe.gui.widget.TileWidget;
import marten.aoe.loader.Loader;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class Game extends AgeScene {
    private static org.apache.log4j.Logger log = Logger.getLogger(Game.class);
    private HashMap<String, ImageData> terrainCache = new HashMap<String, ImageData>();

    private Flatland flatland = new Flatland();
    private BitmapFont font = FontCache.getFont(new Font("Courier New",
            Font.BOLD, 20));

    public Game(String mapName) {
        KeyboardController keyboardController = new KeyboardController();
        keyboardController.addListener(new KeyboardListener() {
            @Override
            public void keyUp(int key, char character) {
            }
            
            @Override
            public void keyDown(int key, char character) {
                if (key == Keyboard.KEY_DOWN) {
                    flatland.scrollDown(5);
                }
            }
        });
        this.addController(keyboardController);

        this.addController(new KeyboardController());
        log.info("Loading map data for '" + mapName + "'...");
        try {
            Loader.load("data/maps/MapTest");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Loaded.");
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
            TileWidget tileWidget = new TileWidget(terrainCache.get(tile
                    .terrain().name()), tile.at());
            flatland.addChild(tileWidget);
            BitmapString coords = new BitmapString(font, tile.at().x() + ""
                    + tile.at().y());
            flatland.addText(coords, new Point(tileWidget.getPosition()));
        }
    }

    @Override
    public void compute() {
    }

    @Override
    public void render() {
        flatland.render();
    }

}
