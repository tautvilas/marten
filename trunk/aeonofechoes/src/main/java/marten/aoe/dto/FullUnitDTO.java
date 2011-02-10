package marten.aoe.dto;

import java.io.Serializable;

public class FullUnitDTO implements Serializable {
    private static final long serialVersionUID = -8779617899297060459L;
    private final String name;
    private final UnitSize size;
    private final UnitType type;
    private final int movementAllowance;
    private final int maximumMovementAllowance;
    private final DamageResistanceDTO damageResistance;
    private final int hitPoints;
    private final int maxHitPoints;
    private final int detectionRange;
    private final String[] specialFeatures;
    private final PlayerDTO owner;
    public FullUnitDTO (String name, UnitSize size, UnitType type, int movementAllowance, int maximumMovementAllowance, DamageResistanceDTO damageResistance, int hitPoints, int maxHitPoints, int detectionRange, String[] specialFeatures, PlayerDTO owner) {
        this.name = name;
        this.size = size;
        this.type = type;
        this.movementAllowance = movementAllowance;
        this.maximumMovementAllowance = maximumMovementAllowance;
        this.damageResistance = damageResistance;
        this.hitPoints = hitPoints;
        this.maxHitPoints = maxHitPoints;
        this.detectionRange = detectionRange;
        this.specialFeatures = specialFeatures;
        this.owner = owner;
    }
    public String getName () {
        return this.name;
    }
    public UnitSize getSize () {
        return this.size;
    }
    public UnitType getType () {
        return this.type;
    }
    public int getMovementAllowance () {
        return this.movementAllowance;
    }
    public int getMaximumMovementAllowance () {
        return this.maximumMovementAllowance;
    }
    public DamageResistanceDTO getDamageResistance () {
        return this.damageResistance;
    }
    public int getHitPoints () {
        return this.hitPoints;
    }
    public int getMaximumHitPoints () {
        return this.maxHitPoints;
    }
    public int getDetectionRange () {
        return this.detectionRange;
    }
    public String[] getSpecialFeatures () {
        return this.specialFeatures;
    }
    public PlayerDTO getOwner () {
        return this.owner;
    }
}
