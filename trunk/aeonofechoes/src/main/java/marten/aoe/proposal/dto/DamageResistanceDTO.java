package marten.aoe.proposal.dto;

import java.util.EnumMap;

public final class DamageResistanceDTO {
    private final EnumMap<DamageType, Integer> damageResistance;
    public DamageResistanceDTO(EnumMap<DamageType, Integer> damageResistance) {
        this.damageResistance = damageResistance;
    }
    public int getDamageResistance(DamageType damageType) {
        if (damageResistance.containsKey(damageType))
            return this.damageResistance.get(damageType);
        return 0;
    }
}
