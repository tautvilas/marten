package marten.aoe.server.serializable;

import java.io.Serializable;

public class GameDetails implements Serializable {
    private static final long serialVersionUID = 3089867646546555166L;

    private String creator;
    private String[] players;
    private String gameName;
    private String mapName;
    private boolean open = true;

    public GameDetails(String creator, String map, String gameName, String[] players, boolean open) {
        this.creator = creator;
        this.mapName = map;
        this.gameName = gameName;
        this.players = players;
        this.open = open;
    }

    public int getNumPlayers() {
        return this.players.length;
    }

    public String[] getPlayers() {
        return players;
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
