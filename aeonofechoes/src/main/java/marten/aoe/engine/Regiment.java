package marten.aoe.engine;

import marten.aoe.dto.DamageDTO;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.UnitSize;
import marten.aoe.dto.UnitType;

public abstract class Regiment extends Unit {
    private final DamageDTO meleeDamage;
    private final DamageDTO rangedDamage;
    private final int attackRange;
    public Regiment(Regiment other, Tile location) {
        super(other, location);
        this.attackRange = other.attackRange;
        this.meleeDamage = other.meleeDamage;
        this.rangedDamage = other.rangedDamage;
    }
    public Regiment(String name, Tile location, PlayerDTO owner, UnitSize unitSize, UnitType unitType, int movementAllowance, int hitPoints, int detectionRange, int detectionModifier, DamageDTO meleeDamage, DamageDTO rangedDamage, int attackRange) {
        super(name, location, owner, unitSize, unitType, movementAllowance, hitPoints,
                detectionRange, detectionModifier);
        this.attackRange = attackRange;
        this.meleeDamage = meleeDamage;
        this.rangedDamage = rangedDamage;
    }
    public DamageDTO getMeleeDamage() {
        return this.meleeDamage;
    }
    public DamageDTO getRangedDamage() {
        return this.rangedDamage;
    }
    public int getAttackRange() {
        return this.attackRange;
    }
}
