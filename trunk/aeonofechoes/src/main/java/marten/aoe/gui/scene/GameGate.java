package marten.aoe.gui.scene;

import java.net.InetAddress;
import java.rmi.Naming;

import marten.age.core.AgeScene;
import marten.age.graphics.flat.Flatland;
import marten.aoe.GameInfo;
import marten.aoe.server.AoeServer;
import marten.aoe.server.ClientSession;
import marten.aoe.server.face.Server;
import marten.aoe.server.face.ServerGameGate;

public class GameGate extends AgeScene {

    private ServerGameGate gate;
    private String serverUrl;
    private ClientSession session;
    private Flatland flatland = new Flatland();

    public GameGate(String mapName) {
        AoeServer.start();
        try {
            Server gameServer = connect(InetAddress.getByName("localhost"));
            this.session = new ClientSession(GameInfo.nickname);
            gameServer.login(session);
            gameServer.createGame(session, "default", mapName);
            this.gate = (ServerGameGate)Naming.lookup(gameServer
                    .getGateUrl("default"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public GameGate(InetAddress server) {
        Server gameServer = connect(server);
        this.session = new ClientSession(GameInfo.nickname);
        try {
            gameServer.login(session);
            this.gate = (ServerGameGate)Naming.lookup(gameServer
                    .getGateUrl("default"));
            this.gate.join(this.session);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        flatland.render();
    }

}
