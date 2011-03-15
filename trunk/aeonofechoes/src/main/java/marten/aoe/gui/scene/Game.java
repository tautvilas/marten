package marten.aoe.gui.scene;

import java.awt.Font;
import java.rmi.RemoteException;
import java.util.LinkedList;

import marten.age.control.KeyboardController;
import marten.age.control.KeyboardListener;
import marten.age.control.MouseController;
import marten.age.core.AgeScene;
import marten.age.core.AppInfo;
import marten.age.graphics.appearance.Color;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import marten.age.widget.Action;
import marten.age.widget.Button;
import marten.age.widget.obsolete.FpsCounter;
import marten.aoe.GameInfo;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.gui.MapWidgetListener;
import marten.aoe.gui.widget.AoeButtonFactory;
import marten.aoe.gui.widget.MapWidget;
import marten.aoe.gui.widget.Sidebar;
import marten.aoe.server.NetworkListener;
import marten.aoe.server.face.EngineFace;
import marten.aoe.server.serializable.EngineEvent;
import marten.aoe.server.serializable.GameDetails;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class Game extends AgeScene implements MapWidgetListener {
    private final int MAP_SCROLL_SPEED = 30;

    private static org.apache.log4j.Logger log = Logger.getLogger(Game.class);

    private Flatland flatland;
    private MapWidget map;
    @SuppressWarnings("unused")
    private GameDetails params;
    private EngineFace engine;
    private Sidebar sidebar;
    private MouseController mouseController = new MouseController();
    private NetworkListener listener;
    private BitmapString turnNotify = null;
    private boolean unitCreated = false;
    private LinkedList<TileDTO> updatedTiles = new LinkedList<TileDTO>();
    private LinkedList<EngineEvent> events = new LinkedList<EngineEvent>();

    @SuppressWarnings("deprecation")
    public Game(EngineFace engineFace, GameDetails details) {
        this.params = details;
        this.engine = engineFace;
        try {
            this.map = new MapWidget(this.engine.getMap(),
                    new Dimension(AppInfo.getDisplayWidth() - 180, AppInfo
                            .getDisplayHeight()), this);
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
        turnNotify = new BitmapString(FontCache.getFont(new Font("Arial",
                Font.PLAIN, 20)));
        turnNotify.setPosition(new Point(AppInfo.getDisplayWidth() - 150,
                AppInfo.getDisplayHeight() - 50));
        flatland.addChild(turnNotify);
        endTurnButton.setAction(new Action() {
            @Override
            public void perform() {
                try {
                    engine.endTurn();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        flatland.addChild(new FpsCounter());

        KeyboardController keyboardController = new KeyboardController();
        keyboardController.addListener(new KeyboardListener() {
            @Override
            public void keyUp(int key, char character) {
            }

            @Override
            public void keyDown(int key, char character) {
                if (key == Keyboard.KEY_DOWN) {
                    map.ScrollDown(MAP_SCROLL_SPEED);
                } else if (key == Keyboard.KEY_UP) {
                    map.ScrollUp(MAP_SCROLL_SPEED);
                } else if (key == Keyboard.KEY_LEFT) {
                    map.ScrollLeft(MAP_SCROLL_SPEED);
                } else if (key == Keyboard.KEY_RIGHT) {
                    map.ScrollRight(MAP_SCROLL_SPEED);
                }
            }
        });
        this.addController(keyboardController);
        MouseController mouseController = new MouseController();
        this.addController(mouseController);
        this.registerControllable(map);
        this.registerControllable(endTurnButton);
        flatland.compile();
        try {
            if (this.engine.getActivePlayer().getName().equals(
                    GameInfo.nickname)) {
                this.turnNotify.setContent("Your turn");
                this.turnNotify.setColor(new Color(0, 1, 0));
                // log.info("##### " + this.engine.getStartPosition());
                this.engine.createUnit("Dwarf", this.engine.getStartPosition());
                log.info("Game scene is initialized. Active player is '"
                        + this.engine.getActivePlayer().getName() + "'");
                this.unitCreated = true;
            } else {
                this.turnNotify.setContent("Not your turn");
                this.turnNotify.setColor(new Color(1, 0, 0));
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        this.registerNetworkListener();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void compute() {
        // position map on the screen
        Point coords = mouseController.getMouseCoordinates();
        if (coords.x < 5) {
            map.ScrollLeft(this.MAP_SCROLL_SPEED);
        } else if (coords.x > AppInfo.getDisplayWidth() - 5
                && coords.x < AppInfo.getDisplayWidth()) {
            map.ScrollRight(this.MAP_SCROLL_SPEED);
        } else if (coords.y < 5) {
            map.ScrollDown(this.MAP_SCROLL_SPEED);
        } else if (coords.y > AppInfo.getDisplayHeight() - 5) {
            map.ScrollUp(this.MAP_SCROLL_SPEED);
        }
        // check for new engine events
        synchronized(this.events) {
            while(!this.events.isEmpty()) {
                EngineEvent event = this.events.pop();
                if (event == EngineEvent.TURN_END) {
                    try {
                        if (engine.getActivePlayer().getName().equals(
                                GameInfo.nickname)) {
                            if (!unitCreated) {
                                engine.createUnit("Elf", engine
                                        .getStartPosition());
                                unitCreated = true;
                            }
                            turnNotify.setContent("Your turn");
                            turnNotify.setColor(new Color(0, 1, 0));
                        } else {
                            turnNotify.setContent("Not your turn");
                            turnNotify.setColor(new Color(1, 0, 0));
                        }
                    } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        // check for map tile updates
        synchronized (this.updatedTiles) {
            while (!this.updatedTiles.isEmpty()) {
                map.updateTile(this.updatedTiles.pop());
            }
        }
    }

    @Override
    public void render() {
        flatland.render();
    }

    private void registerNetworkListener() {
        listener = new NetworkListener() {
            @Override
            public void listen() {
                EngineEvent event;
                try {
                    event = engine.listen();
                    if (event == EngineEvent.TILE_UPDATE) {
                        synchronized (updatedTiles) {
                            updatedTiles.add(engine.popTile());
                        }
                    } else if (event == EngineEvent.STREAM_UPDATE) {
                        LinkedList<TileDTO> tiles = engine.popStream();
                        synchronized (updatedTiles) {
                            for (TileDTO tile : tiles) {
                                updatedTiles.add(tile);
                            }
                        }
                    } else {
                        synchronized(events) {
                            events.add(event);
                        }
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        listener.start();
    }

    public void cleanup() {
        this.listener.quit();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void moveUnit(PointDTO from, PointDTO to) {
        try {
            this.engine.moveUnit(from, to);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
