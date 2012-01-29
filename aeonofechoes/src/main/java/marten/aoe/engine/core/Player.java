package marten.aoe.engine.core;

import marten.aoe.dto.PlayerDTO;


public final class Player {
    public static final Player SYSTEM = new Player(0, "System");
    private int team;
    private String name;
    private int money = 0;
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
    public int getMoney() {
        return this.money;
    }
    public void setMoney(int money) {
        this.money = money;
    }
    public PlayerDTO getDTO() {
        return new PlayerDTO(team, name, money);
    }
}
