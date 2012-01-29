package marten.aoe.engine.core;

import marten.aoe.dto.PlayerDTO;
import marten.aoe.engine.Engine;


public final class Player {
    public static final Player SYSTEM = new Player(0, "System", null);
    private int team;
    private String name;
    private int money = 0;
    private Engine owner;
    public Player (int team, String name, Engine owner) {
        this.team = team;
        this.name = name;
        this.owner = owner;
    }
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
    public Engine getOwner() {
        return this.owner;
    }
    public void setOwner(Engine owner) {
        this.owner = owner;
    }
    public void setMoney(int money) {
        this.money = money;
    }
    public PlayerDTO getDTO() {
        return new PlayerDTO(team, name, money);
    }
}
