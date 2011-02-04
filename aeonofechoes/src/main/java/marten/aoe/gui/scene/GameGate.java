package marten.aoe.gui.scene;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;

import marten.age.control.KeyboardController;
import marten.age.control.MouseController;
import marten.age.core.AgeScene;
import marten.age.core.AppInfo;
import marten.age.event.AgeSceneSwitchEvent;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.layout.SimpleLayout;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.transform.TranslationGroup;
import marten.age.widget.Action;
import marten.aoe.GameInfo;
import marten.aoe.gui.scene.menu.MainMenu;
import marten.aoe.gui.widget.AoeField;
import marten.aoe.gui.widget.AoeString;
import marten.aoe.gui.widget.OkCancelDialog;
import marten.aoe.server.AoeServer;
import marten.aoe.server.ServerListener;
import marten.aoe.server.face.Server;
import marten.aoe.server.serializable.ClientSession;
import marten.aoe.server.serializable.GameDetails;
import marten.aoe.server.serializable.ServerNotification;

public class GameGate extends AgeScene {

    private Server gameServer;
    private String serverUrl;
    private ClientSession session;
    private Flatland flatland = new Flatland();
    private TranslationGroup players = new TranslationGroup();
    private OkCancelDialog dialog;
    private ServerListener listener;

    private GameGate() {
        Dimension dApp = AppInfo.getDisplayDimension();
        AoeField chatField = new AoeField();
        dialog = new OkCancelDialog(new Dimension(600, 200));
        AoeString waiting = new AoeString("Waiting for players to join...");
        AoeString player = new AoeString(GameInfo.nickname);
        player.setColor(new marten.age.graphics.appearance.Color(0, 1.0, 0.0));
        player.setPosition(new Point(200, 200));
        players.addChild(player);
        SimpleLayout layout = new SimpleLayout(dApp);
        dialog.setPosition(new Point(dApp.width - 600, 0));
        flatland.addChild(dialog);
        layout.centerHorizontally(waiting, (int)(dApp.height - 100));
        flatland.addChild(layout);
        flatland.addChild(players);
        dialog.setOkAction(new Action() {
            @Override
            public void perform() {
            }
        });
        dialog.setCancelAction(new Action() {
            @Override
            public void perform() {
                try {
                    listener.quit();
                    gameServer.leave(session);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                fireEvent(new AgeSceneSwitchEvent(new MainMenu()));
            }
        });
        this.addController(new MouseController());
        this.addController(new KeyboardController());
        this.registerControllable(chatField);
        this.registerControllable(dialog);
        this.flatland.compile();
    }

    /**
     * Host network game
     * 
     * @param mapName
     *            map name
     */
    public GameGate(String mapName) {
        this();
        AoeServer.start();
        try {
            this.gameServer = connect(InetAddress.getByName("localhost"));
            this.session = gameServer.login(GameInfo.nickname);
            gameServer.createGame(session, "default", mapName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.registerListeners();
        dialog.setOkAction(new Action() {
            @Override
            public void perform() {
                try {
                    gameServer.startGame(session, "default");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Join network game
     * 
     * @param server
     *            network server address
     */
    public GameGate(InetAddress server) {
        this();
        this.gameServer = connect(server);
        try {
            this.session = gameServer.login(GameInfo.nickname);
            this.gameServer.joinGame(this.session, "default");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.registerListeners();
    }

    private void registerListeners() {
        this.listener = new ServerListener(this.gameServer, this.session) {
            @Override
            public void listen() {
                ServerNotification notification;
                try {
                    notification = gameServer.listen(session);
                    System.out.println(notification);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                if (notification == ServerNotification.PLAYER_LIST_UPDATED) {
                    String[] members;
                    try {
                        members = gameServer.getGameDetails(session, "default").players;
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    flatland.removeChild(players);
                    players = new TranslationGroup();
                    for (int i = 0; i < members.length; i++) {
                        AoeString player = new AoeString(members[i]);
                        player
                                .setColor(new marten.age.graphics.appearance.Color(
                                        0, 1.0, 0.0));
                        player.setPosition(new Point(200, 200 + 50 * i));
                        players.addChild(player);
                    }
                    flatland.addChild(players);
                } else if (notification == ServerNotification.GAME_STARTED) {
                    GameDetails details;
                    try {
                        details = gameServer.getGameDetails(session, "default");
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    fireEvent(new AgeSceneSwitchEvent(new GameLoader(details)));
                }
            }
        };
    }

    private Server connect(InetAddress server) {
        this.serverUrl = "rmi://" + server.getHostAddress() + "/Server";
        Server gameServer = null;
        try {
            gameServer = (Server)Naming.lookup(serverUrl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return gameServer;
    }

    @Override
    public void compute() {
    }

    @Override
    public void render() {
        this.flatland.render();
    }

}
