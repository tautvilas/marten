package marten.aoe.gui.scene;

import java.io.IOException;

import marten.age.control.KeyboardController;
import marten.age.control.KeyboardListener;
import marten.age.core.AgeScene;
import marten.age.graphics.flat.Flatland;
import marten.aoe.gui.widget.MapWidget;
import marten.aoe.loader.Loader;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class Game extends AgeScene {
    private static org.apache.log4j.Logger log = Logger.getLogger(Game.class);

    private Flatland flatland = new Flatland();

    public Game(String mapName) {
        KeyboardController keyboardController = new KeyboardController();
        keyboardController.addListener(new KeyboardListener() {
            @Override
            public void keyUp(int key, char character) {
            }

            @Override
            public void keyDown(int key, char character) {
                if (key == Keyboard.KEY_DOWN) {
                    // flatland.scrollDown(5);
                }
            }
        });
        this.addController(keyboardController);

        log.info("Loading map data for '" + mapName + "'...");
        try {
            Loader.load("data/maps/MapTest");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Loaded.");
        MapWidget map = new MapWidget(mapName);
        flatland.addChild(map);
    }

    @Override
    public void compute() {
    }

    @Override
    public void render() {
        flatland.render();
    }

}
