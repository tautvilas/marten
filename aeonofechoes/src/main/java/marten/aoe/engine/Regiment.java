package marten.aoe.engine;

import marten.aoe.dto.DamageDTO;
import marten.aoe.dto.PointDTO;
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
    public Regiment(String name, Tile location, Player owner, UnitSize unitSize, UnitType unitType, int movementAllowance, int hitPoints, int detectionRange, int detectionModifier, DamageDTO meleeDamage, DamageDTO rangedDamage, int attackRange) {
        super(name, location, owner, unitSize, unitType, movementAllowance, hitPoints,
                detectionRange, detectionModifier);
        this.attackRange = attackRange;
        this.meleeDamage = meleeDamage;
        this.rangedDamage = rangedDamage;
    }
    /** Special action 1 for regiments is movement.*/
    @Override public final void specialAction1(PointDTO target) {
        this.getMap().moveUnit(this.getOwner(), this.getLocation().getCoordinates(), target);
    }
    /** Special action 2 for regiments is melee attack.*/
    @Override public final void specialAction2(PointDTO target) {
        Tile targetTile = this.getMap().getTile(target);
        if (targetTile.distanceTo(this.getLocation()) != 1) {
            return;
        }
        targetTile.applyDamage(this.meleeDamage);
    }
    /** Special action 3 for regiments is ranged attack.*/
    @Override public void specialAction3(PointDTO target) {
        if (this.attackRange < 2) {
            return;
        }
        Tile targetTile = this.getMap().getTile(target);
        if (targetTile.distanceTo(this.getLocation()) < 2 && targetTile.distanceTo(this.getLocation()) > this.attackRange) {
            return;
        }
        targetTile.applyDamage(this.rangedDamage);
    }
}
