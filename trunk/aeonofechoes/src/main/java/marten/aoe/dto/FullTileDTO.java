package marten.aoe.dto;

import java.io.Serializable;

public final class FullTileDTO implements Serializable {
    private static final long serialVersionUID = 8570422115600942749L;
    private final FullUnitDTO unit;
    private final PointDTO location;
    private final String name;
    private final MovementDTO movement;
    private final DefenseDTO defense;
    private final int height;
    private final String[] description;
    private final boolean visible;

    public FullTileDTO (String name, PointDTO location, int height, MovementDTO movement, DefenseDTO defense, FullUnitDTO unit, String[] description, boolean visible) {
        this.name = name;
        this.location = location;
        this.movement = movement;
        this.defense = defense;
        this.unit = unit;
        this.height = height;
        this.description = description;
        this.visible = visible;
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
    public FullUnitDTO getUnit () {
        return this.unit;
    }
    public String[] getDescription () {
        return this.description;
    }
    public boolean getVisibility () {
        return this.visible;
    }
}
