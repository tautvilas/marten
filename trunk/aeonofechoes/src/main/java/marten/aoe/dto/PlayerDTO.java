package marten.aoe.dto;

import java.io.Serializable;


public class PlayerDTO implements Serializable {
    private static final long serialVersionUID = 1L;
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
