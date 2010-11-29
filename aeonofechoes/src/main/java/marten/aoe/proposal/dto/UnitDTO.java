package marten.aoe.proposal.dto;

import java.io.Serializable;

public class UnitDTO implements Serializable {
    private static final long serialVersionUID = -8779617899297060459L;
    private final String name;
    private final UnitSize size;
    private final UnitType type;
    private final int movementAllowance;
    private final int maximumMovementAllowance;
    public UnitDTO (String name, UnitSize size, UnitType type, int movementAllowance, int maximumMovementAllowance) {
        this.name = name;
        this.size = size;
        this.type = type;
        this.movementAllowance = movementAllowance;
        this.maximumMovementAllowance = maximumMovementAllowance;
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
}
