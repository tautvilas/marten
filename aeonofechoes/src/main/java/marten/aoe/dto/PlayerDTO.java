package marten.aoe.dto;

import java.io.Serializable;


public class PlayerDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int team;
    private final String name;
    private final int energy;
    private final int energyRate;
    private final int energyCapacity;
    public PlayerDTO (int team, String name, int energy, int energyRate, int energyCapacity) {
        this.team = team;
        this.name = name;
        this.energy = energy;
        this.energyRate = energyRate;
        this.energyCapacity = energyCapacity;
    }
    public final int getTeam() {
        return this.team;
    }
    public final String getName() {
        return this.name;
    }
    public final int getEnergy() {
        return this.energy;
    }
    public final int getEnergyRate() {
        return this.energyRate;
    }
    public final int getEnergyCapacity() {
        return this.energyCapacity;
    }
}
