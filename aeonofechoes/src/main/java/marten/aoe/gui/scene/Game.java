package marten.aoe.gui.scene;

import java.io.IOException;

import marten.age.control.KeyboardController;
import marten.age.control.KeyboardListener;
import marten.age.control.MouseController;
import marten.age.core.AgeScene;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.primitives.Point;
import marten.aoe.gui.widget.MapWidget;
import marten.aoe.loader.Loader;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class Game extends AgeScene {
    private static org.apache.log4j.Logger log = Logger.getLogger(Game.class);

    private Flatland flatland = new Flatland();
    private MapWidget map;

    public Game(String mapName) {
        log.info("Loading map data for '" + mapName + "'...");
        try {
            Loader.load("data/maps/MapTest");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Loaded.");
        map = new MapWidget(mapName);
        map.setPosition(new Point(200, 200));
        flatland.addChild(map);

        KeyboardController keyboardController = new KeyboardController();
        keyboardController.addListener(new KeyboardListener() {
            @Override
            public void keyUp(int key, char character) {
            }

            @Override
            public void keyDown(int key, char character) {
                if (key == Keyboard.KEY_DOWN) {
                    map.setPosition(new Point(map.getPosition().x, map.getPosition().y - 10));
                } else if (key == Keyboard.KEY_UP) {
                    map.setPosition(new Point(map.getPosition().x, map.getPosition().y + 10));
                } else if (key == Keyboard.KEY_LEFT) {
                    map.setPosition(new Point(map.getPosition().x - 10, map.getPosition().y));
                } else if (key == Keyboard.KEY_RIGHT) {
                    map.setPosition(new Point(map.getPosition().x + 10, map.getPosition().y));
                }
            }
        });
        this.addController(keyboardController);
        this.addController(new MouseController());
        this.registerControllable(map);
    }

    @Override
    public void compute() {
    }

    @Override
    public void render() {
        flatland.render();
    }

}
