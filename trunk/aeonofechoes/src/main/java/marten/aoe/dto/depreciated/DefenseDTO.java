package marten.aoe.dto.depreciated;

import java.io.Serializable;
import java.util.EnumMap;

import marten.aoe.data.type.UnitSize;
import marten.aoe.data.type.UnitType;

public final class DefenseDTO implements Serializable {
    private static final long serialVersionUID = -4521243019926967774L;
    private final EnumMap<UnitType, EnumMap<UnitSize, Integer>> data;
    public DefenseDTO (EnumMap<UnitType, EnumMap<UnitSize, Integer>> values) {
        this.data = values;    
    }
    public int getValue(UnitSize size, UnitType type) {
        if (this.data.containsKey(type))
            if (this.data.get(type).containsKey(size))
                return this.data.get(type).get(size).intValue();
        return 0;
    }
}
