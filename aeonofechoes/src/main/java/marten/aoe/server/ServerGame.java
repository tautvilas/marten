package marten.aoe.server;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.util.ArrayList;

import marten.aoe.dto.PlayerDTO;
import marten.aoe.engine.Engine;
import marten.aoe.server.serializable.GameDetails;
import marten.aoe.server.serializable.ServerNotification;

public class ServerGame {
    private final ServerClient creator;
    private final ArrayList<ServerClient> players = new ArrayList<ServerClient>();
    private final String gameName;
    private final String mapName;
    private Engine engine;
    private final String serverUrl;
    private boolean open = true;

    public ServerGame(ServerClient creator, String map, String gameName,
            String serverUrl) {
        this.creator = creator;
        this.mapName = map;
        this.serverUrl = serverUrl;
        this.gameName = gameName;
        this.players.add(creator);
    }

    public void addPlayer(ServerClient user) {
        this.players.add(user);
        this.notify(ServerNotification.PLAYER_LIST_UPDATED);
    }

    public int getNumPlayers() {
        return this.players.size();
    }

    public void removePlayer(ServerClient client) throws RemoteException {
        if (this.players.contains(client)) {
            if (client.getEngineUrl() != null) {
                try {
                    Naming.unbind(client.getEngineUrl());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (NotBoundException e) {
                    e.printStackTrace();
                }
            }
            this.players.remove(client);
            this.notify(ServerNotification.PLAYER_LIST_UPDATED);
        }
    }

    public boolean hasPlayer(ServerClient user) {
        return this.players.contains(user);
    }

    public String getGameName() {
        return this.gameName;
    }

    public String getMapName() {
        return this.mapName;
    }

    public boolean isOpen() {
        return this.open;
    }

    public ServerClient getCreator() {
        return this.creator;
    }

    public GameDetails getDetails(ServerClient client) throws RemoteException {
        String[] members = new String[this.players.size()];
        for (int i = 0; i < members.length; i++) {
            members[i] = this.players.get(i).getUsername();
        }
        GameDetails details = new GameDetails();
        details.creator = this.creator.getUsername();
        details.mapName = this.mapName;
        details.gameName = this.gameName;
        details.players = members;
        details.open = this.open;
        details.engineUrl = client.getEngineUrl();
        return details;
    }

    public ArrayList<ServerClient> getPlayers() {
        return this.getPlayers();
    }

    public void start() throws RemoteException {
        this.open = false;
        PlayerDTO[] players = new PlayerDTO[this.players.size()];
        for (int i = 0; i < players.length; i++) {
            ServerClient client = this.players.get(i);
            players[i] = new PlayerDTO(i, client.getUsername());
        }
        this.engine = new Engine(this.mapName, players);
        this.engine.start();
        for (int i = 0; i < players.length; i++) {
            ServerClient client = this.players.get(i);
            String url = this.serverUrl + "/" + client.getUsername()
                    + new BigInteger(130, new SecureRandom()).toString(32);
            client.setEngineUrl(url);
            try {
                Naming.rebind(client.getEngineUrl(), new EngineInterface(
                        this.engine, players[i]));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        this.notify(ServerNotification.GAME_STARTED);
    }

    public void notify(ServerNotification notification) {
        for (ServerClient client : this.players) {
            client.notify(notification);
        }
    }
}
