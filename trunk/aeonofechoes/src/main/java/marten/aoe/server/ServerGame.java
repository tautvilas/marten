package marten.aoe.server;

import java.util.ArrayList;

import marten.aoe.server.serializable.GameDetails;
import marten.aoe.server.serializable.ServerNotification;

public class ServerGame {
    private ServerClient creator;
    private ArrayList<ServerClient> players = new ArrayList<ServerClient>();
    private String gameName;
    private String mapName;
    private boolean open = true;

    public ServerGame(ServerClient creator, String map, String gameName) {
        this.creator = creator;
        this.mapName = map;
        this.gameName = gameName;
        players.add(creator);
    }

    public void addPlayer(ServerClient user) {
        this.players.add(user);
        this.notify(ServerNotification.PLAYER_LIST_UPDATED);
    }

    public int getNumPlayers() {
        return this.players.size();
    }

    public void removePlayer(ServerClient client) {
        if (players.contains(client)) {
            players.remove(client);
            this.notify(ServerNotification.PLAYER_LIST_UPDATED);
        }
    }

    public boolean hasPlayer(ServerClient user) {
        return players.contains(user);
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

    public GameDetails getDetails() {
        String[] members = new String[players.size()];
        for (int i = 0; i < members.length; i++) {
            members[i] = players.get(i).getUsername();
        }
        return new GameDetails(this.creator.getUsername(), this.mapName,
                this.gameName, members, this.open);
    }

    public ArrayList<ServerClient> getPlayers() {
        return this.getPlayers();
    }

    public void start() {
        this.open = false;
        this.notify(ServerNotification.GAME_STARTED);
    }

    public void notify(ServerNotification notification) {
        for (ServerClient client : players) {
            client.notify(notification);
        }
    }
}
