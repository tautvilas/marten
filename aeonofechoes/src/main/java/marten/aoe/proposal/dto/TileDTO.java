package marten.aoe.proposal.dto;

public final class TileDTO {
    private final UnitDTO unit;
    private final Point location;
    private final String name;
    private final MovementDTO movement;
    private final DefenseDTO defense;
    private final int height;
    private final String[] description;
    
    public TileDTO (String name, Point location, int height, MovementDTO movement, DefenseDTO defense, UnitDTO unit, String[] description) {
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
    public Point getLocation () {
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
