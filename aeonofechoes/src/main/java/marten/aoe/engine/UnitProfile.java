package marten.aoe.engine;

public final class UnitProfile {
    private String name;
    private UnitType type;
    private int maxMovement;
    public UnitProfile(UnitProfileDatabase database, String name, UnitType type, int maxMovement) {
        this.name = name;
        this.type = type;
        this.maxMovement = maxMovement;
        database.add(this);
    }
    public String name() {
        return this.name;
    }
    public UnitType type() {
        return this.type;
    }
    public int maxMovement() {
        return this.maxMovement;
    }
}
