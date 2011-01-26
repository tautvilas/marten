package marten.aoe.dto;

import java.io.Serializable;

public final class TileDTO implements Serializable {
    private static final long serialVersionUID = 8570422115600942749L;
    private final UnitDTO unit;
    private final PointDTO location;
    private final String name;
    private final MovementDTO movement;
    private final DefenseDTO defense;
    private final int height;
    private final String[] description;
    
    public TileDTO (String name, PointDTO location, int height, MovementDTO movement, DefenseDTO defense, UnitDTO unit, String[] description) {
        this.name = name;
        this.location = location;
        this.movement = movement;
        this.defense = defense;
        this.unit = unit;
        this.height = height;
        this.description = description;
    }
    public String getName () {
        return this.name;
    }
    public PointDTO getLocation () {
        return this.location;
    }
    public int getHeight () {
        return this.height;
    }
    public MovementDTO getMovement () {
        return this.movement;
    }
    public DefenseDTO getDefense () {
        return this.defense;
    }
    public UnitDTO getUnit () {
        return this.unit;
    }
    public String[] description () {
        return this.description;
    }
}
