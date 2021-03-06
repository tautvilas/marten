package marten.aoe.dto.depreciated;

import marten.aoe.data.type.DamageType;

public final class DamageDTO {
    private final int maxDamage;
    private final DamageType damageType;
    public DamageDTO (int maxDamage, DamageType damageType) {
        this.maxDamage = maxDamage;
        this.damageType = damageType;
    }
    public int getMaxDamage() {
        return this.maxDamage;
    }
    public DamageType getDamageType() {
        return this.damageType;
    }
}
