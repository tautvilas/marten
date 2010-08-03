package marten.aoe.gui.scene;

import java.io.IOException;

import marten.age.control.KeyboardController;
import marten.age.control.KeyboardListener;
import marten.age.control.MouseController;
import marten.age.core.AgeScene;
import marten.age.core.AppInfo;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.primitives.Point;
import marten.age.widget.obsolete.FpsCounter;
import marten.aoe.engine.Engine;
import marten.aoe.gui.widget.MapWidget;
import marten.aoe.loader.Loader;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class Game extends AgeScene {
    private static org.apache.log4j.Logger log = Logger.getLogger(Game.class);

    private Flatland flatland = new Flatland();
    private Engine engine = new Engine();
    private MapWidget map;    
    private MouseController mouseController = new MouseController();

    public Game(String mapName) {
        log.info("Loading map data for '" + mapName + "'...");
        try {
            Loader.load(this.engine, "data/maps/2pFreelands");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Loaded.");
        map = new MapWidget(this.engine, mapName);
        flatland.addChild(map);
        flatland.addChild(new FpsCounter());

        KeyboardController keyboardController = new KeyboardController();
        keyboardController.addListener(new KeyboardListener() {
            @Override
            public void keyUp(int key, char character) {
            }

            @Override
            public void keyDown(int key, char character) {
                if (key == Keyboard.KEY_DOWN) {
                    map.ScrollDown(20);
                } else if (key == Keyboard.KEY_UP) {
                    map.ScrollUp(20);
                } else if (key == Keyboard.KEY_LEFT) {
                    map.ScrollLeft(20);
                } else if (key == Keyboard.KEY_RIGHT) {
                    map.ScrollRight(20);
                }
            }
        });
        this.addController(keyboardController);
        MouseController mouseController = new MouseController();
        this.addController(mouseController);
        this.registerControllable(map);
        flatland.compile();
    }

    @Override
    public void compute() {
        Point coords = mouseController.getMouseCoordinates();
        if (coords.x < 5) {
            map.ScrollLeft(10);
        } else if (coords.x > AppInfo.getDisplayWidth() - 5) {
            map.ScrollRight(10);
        } else if (coords.y < 5) {
            map.ScrollDown(10);
        } else if (coords.y > AppInfo.getDisplayHeight() - 5) {
            map.ScrollUp(10);
        }
    }

    @Override
    public void render() {
        flatland.render();
    }

}
