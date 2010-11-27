package marten.aoe.proposal.dto;

import java.io.Serializable;
import java.util.EnumMap;

public final class DefenseDTO implements Serializable {
    private static final long serialVersionUID = -4521243019926967774L;
    private final EnumMap<UnitType, EnumMap<UnitSize, Integer>> data = new EnumMap<UnitType, EnumMap<UnitSize, Integer>>(UnitType.class);
    public DefenseDTO (int[][] values) {
        for (UnitType unitType : UnitType.values()) {
            EnumMap<UnitSize, Integer> insideMap = new EnumMap<UnitSize, Integer>(UnitSize.class);
            for (UnitSize unitSize : UnitSize.values()) {
                insideMap.put(unitSize, new Integer(values[unitType.ordinal()][unitSize.ordinal()]));
            }
            this.data.put(unitType, insideMap);
        }        
    }
    public int getValue(UnitSize size, UnitType type) {
        return this.data.get(type).get(size).intValue();
    }
}
