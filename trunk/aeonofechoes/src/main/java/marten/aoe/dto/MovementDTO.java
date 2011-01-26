package marten.aoe.dto;

import java.io.Serializable;
import java.util.EnumMap;

public final class MovementDTO implements Serializable {
    private static final long serialVersionUID = 7333580818044229109L;
    private final EnumMap<UnitType, EnumMap<UnitSize, Integer>> data;
    public MovementDTO (EnumMap<UnitType, EnumMap<UnitSize, Integer>> values) {
        this.data = values;     
    }
    public int getValue(UnitSize size, UnitType type) {
        if (this.data.containsKey(type))
            if (this.data.get(type).containsKey(size))
                return this.data.get(type).get(size).intValue();
        return Integer.MAX_VALUE;
    }
}
