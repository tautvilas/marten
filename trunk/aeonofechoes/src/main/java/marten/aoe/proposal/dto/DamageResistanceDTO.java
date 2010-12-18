package marten.aoe.proposal.dto;

import java.util.EnumMap;

public final class DamageResistanceDTO {
    private final EnumMap<DamageType, Double> damageResistance;
    public DamageResistanceDTO(EnumMap<DamageType, Double> damageResistance) {
        this.damageResistance = damageResistance;
    }
    public double getDamageResistance(DamageType damageType) {
        if (damageResistance.containsKey(damageType))
            return this.damageResistance.get(damageType);
        return 1.0;
    }
}
