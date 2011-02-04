package marten.aoe.gui.scene;

import java.rmi.RemoteException;

import marten.age.control.KeyboardController;
import marten.age.control.KeyboardListener;
import marten.age.control.MouseController;
import marten.age.core.AgeScene;
import marten.age.core.AppInfo;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.widget.Button;
import marten.age.widget.obsolete.FpsCounter;
import marten.aoe.gui.widget.AoeButtonFactory;
import marten.aoe.gui.widget.MapWidget;
import marten.aoe.gui.widget.Sidebar;
import marten.aoe.server.face.EngineFace;
import marten.aoe.server.serializable.GameDetails;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class Game extends AgeScene {
    @SuppressWarnings("unused")
    private static org.apache.log4j.Logger log = Logger.getLogger(Game.class);

    private Flatland flatland;
    private MapWidget map;
    @SuppressWarnings("unused")
    private GameDetails params;
    private EngineFace engine;
    private Sidebar sidebar;
    private MouseController mouseController = new MouseController();

    public Game(EngineFace engine, GameDetails details) {
        this.params = details;
        this.engine = engine;
        try {
            this.map = new MapWidget(this.engine.getMap(),
                    new Dimension(AppInfo.getDisplayWidth() - 180, AppInfo
                            .getDisplayHeight()));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        flatland = new Flatland();

        flatland.addChild(map);
        this.sidebar = new Sidebar(new Dimension(256, AppInfo
                .getDisplayHeight()));
        sidebar.setPosition(new Point(AppInfo.getDisplayWidth() - 180, 0));
        Button endTurnButton = AoeButtonFactory.getEndTurnButton();
        endTurnButton
                .setPosition(new Point(AppInfo.getDisplayWidth() - 150, 25));
        flatland.addChild(sidebar);
        flatland.addChild(endTurnButton);
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
        this.registerControllable(endTurnButton);
        flatland.compile();
    }

    @Override
    public void compute() {
        Point coords = mouseController.getMouseCoordinates();
        if (coords.x < 5) {
            map.ScrollLeft(10);
        } else if (coords.x > sidebar.getPosition().x - 5
                && coords.x < sidebar.getPosition().x) {
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
