package marten.aoe.dto;

import java.util.EnumMap;

public final class DamageResistanceDTO {
    private final EnumMap<DamageType, Integer> damageResistance;
    public DamageResistanceDTO(EnumMap<DamageType, Integer> damageResistance) {
        this.damageResistance = damageResistance;
    }
    public DamageResistanceDTO(int[] damageResistance) {
        this.damageResistance = new EnumMap<DamageType, Integer>(DamageType.class);
        assert (damageResistance.length == DamageType.values().length);
        int index = 0;
        for (DamageType damageType : DamageType.values()) {
            this.damageResistance.put(damageType, damageResistance[index]);
            index++;
        }
    }
    public int getDamageResistance(DamageType damageType) {
        if (this.damageResistance.containsKey(damageType)) {
            return this.damageResistance.get(damageType);
        }
        return 0;
    }
}
