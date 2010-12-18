package marten.aoe.server.serializable;

import java.io.Serializable;

public class GameDetails implements Serializable {
    private static final long serialVersionUID = 3089867646546555166L;

    public String creator;
    public String[] players;
    public String gameName;
    public String mapName;
    public boolean open = true;
    public String engineUrl = "";
}
