package marten.aoe.gui;

public class GameParams {
    private String mapName = "";
    private String gameUrl = "";

    public GameParams(String mapName, String gameUrl) {
        this.mapName = mapName;
        this.gameUrl = gameUrl;
    }

    public String getMapName() {
        return this.mapName;
    }

    public String getGameUrl() {
        return this.gameUrl;
    }
}
