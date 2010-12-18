package marten.aoe.proposal.dto;

import java.io.Serializable;
import java.util.EnumMap;

public final class DefenseDTO implements Serializable {
    private static final long serialVersionUID = -4521243019926967774L;
    private final EnumMap<UnitType, EnumMap<UnitSize, Double>> data;
    public DefenseDTO (EnumMap<UnitType, EnumMap<UnitSize, Double>> values) {
        this.data = values;    
    }
    public double getValue(UnitSize size, UnitType type) {
        if (this.data.containsKey(type))
            if (this.data.get(type).containsKey(size))
                return this.data.get(type).get(size).doubleValue();
        return 0.0;
    }
}
