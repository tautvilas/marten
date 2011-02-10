package marten.aoe.dto;

import java.io.Serializable;


public final class PlayerDTO implements Serializable {
    private static final long serialVersionUID = -8204054815501910153L;
    public static final PlayerDTO SYSTEM = new PlayerDTO(0, "System");
    private final int team;
    private final String name;
    public PlayerDTO (int team, String name) {
        this.team = team;
        this.name = name;
    }
    public final int getTeam() {
        return this.team;
    }
    public final String getName() {
        return this.name;
    }
}
