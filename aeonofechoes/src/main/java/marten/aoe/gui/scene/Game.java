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
import marten.age.graphics.flat.sprite.TextureSprite;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import marten.age.graphics.transform.SymetricScaleGroup;
import marten.age.graphics.transform.TranslationGroup;
import marten.age.widget.Action;
import marten.age.widget.Button;
import marten.age.widget.obsolete.FpsCounter;
import marten.aoe.GameInfo;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.fileio.IoUtil;
import marten.aoe.gui.AoeButtonFactory;
import marten.aoe.gui.MapWidgetListener;
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
    private BitmapString moneyString = null;
    private LinkedList<TileDTO> updatedTiles = new LinkedList<TileDTO>();
    private LinkedList<EngineEvent> events = new LinkedList<EngineEvent>();
    private int selectedAction = 1;

    public Game(EngineFace engineFace, GameDetails details) {
        int appWidth = AppInfo.getDisplayWidth();
        //int appHeight = AppInfo.getDisplayHeight();
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
        moneyString = new BitmapString(FontCache.getFont(new Font("Arial",
                Font.BOLD, 12)));
        moneyString.setPosition(new Point(AppInfo.getDisplayWidth() - 150,
                AppInfo.getDisplayHeight() - 100));
        flatland.addChild(moneyString);
        for (int i = 0; i < 9; i++) {
            Button action = AoeButtonFactory.getActionButton("" + (i + 1));
            float width = action.getDimension().width;
            float height = action.getDimension().height;
            action.setPosition(new Point(appWidth - 7 - width * 3 + i % 3 * width,
                                         300 - i / 3 * height));
            final int act = i + 1;
            action.setAction(new Action() {
                @Override
                public void perform() {
                    Game.this.selectedAction = act;
                }
            });
            this.registerControllable(action);
            flatland.addChild(action);
        }
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
        this.updateMinimap();

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
//        flatland.compile();
        try {
            if (this.engine.getActivePlayer().getName().equals(
                    GameInfo.nickname)) {
                this.turnNotify.setContent("Your turn");
                this.turnNotify.setColor(new Color(0, 1, 0));
                log.info("Game scene is initialized. Active player is '"
                        + this.engine.getActivePlayer().getName() + "'");
            } else {
                this.turnNotify.setContent("Not your turn");
                this.turnNotify.setColor(new Color(1, 0, 0));
            }
            PlayerDTO player = this.engine.getActivePlayer();
            this.moneyString.setContent(player.getMoney() + "");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        this.updateMinimap();
        this.registerNetworkListener();
    }

    private void updateMinimap() {
        TextureSprite minimap = this.map.getMinimap(new Dimension(AppInfo.getDisplayWidth(), AppInfo.getDisplayHeight()));
        SymetricScaleGroup g = new SymetricScaleGroup(0.125);
        TranslationGroup tg = new TranslationGroup();
        tg.addChild(g);
        tg.setId("minimap");
        g.addChild(minimap);
        minimap.setId("minimap");
        tg.setPosition(new Point(AppInfo.getDisplayWidth() - 150, AppInfo.getDisplayHeight() - 300));
        this.flatland.updateChild(tg, -1);
    }

    @Override
    public void compute() {
        long ctype = System.currentTimeMillis();
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
                            turnNotify.setContent("Your turn");
                            turnNotify.setColor(new Color(0, 1, 0));
                        } else {
                            turnNotify.setContent("Not your turn");
                            turnNotify.setColor(new Color(1, 0, 0));
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else if (event == EngineEvent.STATS_UPDATE) {
                    try {
                        PlayerDTO player = engine.getActivePlayer();
                        moneyString.setContent(player.getMoney() + "");
                    } catch (RemoteException e) {
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
        map.animate(10);
        long delta = System.currentTimeMillis() - ctype;
        if (delta > 1) {
            System.err.println("Long computation:" + (delta));
        }
    }

    @Override
    public void render() {
        long ctype = System.currentTimeMillis();
        flatland.render();
        long delta = System.currentTimeMillis() - ctype;
        if (delta > 10) {
            System.err.println("Long render:" + (delta));
        }
    }

    private void registerNetworkListener() {
        listener = new NetworkListener() {
            @Override
            public void listen() {
                EngineEvent event;
                try {
                    event = engine.listen();
                    log.debug("Event caught: " + event);
                    if (event == EngineEvent.TILE_UPDATE) {
                        synchronized (updatedTiles) {
                            updatedTiles.add(engine.popTile());
                        }
                    } else if (event == EngineEvent.STREAM_UPDATE) {
                        LinkedList<TileDTO> tiles = engine.popStream();
                        log.debug("Received stream size: " + IoUtil.getSize(tiles) + "b");
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

    @Override
    public void performAction(PointDTO from, PointDTO to) {
        this.updateMinimap();
        try {
            this.engine.performAction(from, to, Game.this.selectedAction);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
