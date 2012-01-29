package marten.aoe.engine.core;

import marten.aoe.dto.PlayerDTO;


public final class Player {
    public static final Player SYSTEM = new Player(0, "System");
    private final int team;
    private final String name;
    public Player (int team, String name) {
        this.team = team;
        this.name = name;
    }
    public final int getTeam() {
        return this.team;
    }
    public final String getName() {
        return this.name;
    }
    public PlayerDTO getDTO() {
        return new PlayerDTO(team, name);
    }
}
