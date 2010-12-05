package marten.aoe.server.serializable;

import java.io.Serializable;
import java.util.ArrayList;

public class GameDetails implements Serializable {
    private static final long serialVersionUID = 3089867646546555166L;

    private String creator;
    private ArrayList<String> players = new ArrayList<String>();
    private String gameName;
    private String mapName;
    private boolean open = true;

    public GameDetails(String creator, String map, String gameName) {
        this.creator = creator;
        this.mapName = map;
        this.gameName = gameName;
        players.add(creator);
    }

    public void addPlayer(String username) {
        this.players.add(username);
    }

    public int getNumPlayers() {
        return this.players.size();
    }

    public void removePlayer(String username) {
        if (players.contains(username)) {
            players.remove(username);
        }
    }

    public String[] getPlayers() {
        return this.players.toArray(new String[players.size()]);
    }

    public boolean hasPlayer(String username) {
        return players.contains(username);
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

    public String getCreator() {
        return this.creator;
    }
}
