package marten.aoe.engine;

import marten.aoe.dto.Action;
import marten.aoe.dto.DamageDTO;
import marten.aoe.dto.PlayerDTO;
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
    public Regiment(String name, Tile location, PlayerDTO owner, UnitSize unitSize, UnitType unitType, int movementAllowance, int hitPoints, int detectionRange, int detectionModifier, DamageDTO meleeDamage, DamageDTO rangedDamage, int attackRange) {
        super(name, location, owner, unitSize, unitType, movementAllowance, hitPoints,
                detectionRange, detectionModifier);
        this.attackRange = attackRange;
        this.meleeDamage = meleeDamage;
        this.rangedDamage = rangedDamage;
    }
    /** Special actions for regiments.
     * By default action {@code FIRST} is movement, action {@code SECOND} is melee attack, action {@code THIRD} is ranged attack.*/
    @Override public final void specialAction(PointDTO target, Action action) {
        Tile targetTile = this.getMap().getTile(target);
        switch (action) {
            case FIRST:
                this.getMap().moveUnit(this.getOwner(), this.getLocation().getCoordinates(), target);
                break;
            case SECOND:
                if (targetTile.distanceTo(this.getLocation()) != 1) {
                    return;
                }
                targetTile.applyDamage(this.meleeDamage);
            case THIRD:
                if (this.attackRange < 2) {
                    return;
                }
                if (targetTile.distanceTo(this.getLocation()) < 2 && targetTile.distanceTo(this.getLocation()) > this.attackRange) {
                    return;
                }
                targetTile.applyDamage(this.rangedDamage);
            default:
                this.extendedSpecialAction(target, action);
        }

    }
    public abstract void extendedSpecialAction(PointDTO target, Action action);
}
