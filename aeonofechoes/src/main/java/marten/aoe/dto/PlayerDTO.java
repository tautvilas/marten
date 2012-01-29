package marten.aoe.dto;

import java.io.Serializable;


public class PlayerDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int team;
    private final String name;
    private final int money;
    public PlayerDTO (int team, String name, int money) {
        this.team = team;
        this.name = name;
        this.money = money;
    }
    public final int getTeam() {
        return this.team;
    }
    public final String getName() {
        return this.name;
    }
    public final int getMoney() {
        return this.money;
    }
}
