package marten.aoe.depreciated.engine;

public enum UnitType {
    GROUND,
    MARINE,
    AIR;
    public static UnitType fromString(String source) {
        if (source.equals("Ground"))
            return UnitType.GROUND;
        if (source.equals("Marine"))
            return UnitType.MARINE;
        if (source.equals("Air"))
            return UnitType.AIR;
        return null;
    }
}
