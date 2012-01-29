package marten.aoe.data.units;

import marten.aoe.data.type.DamageType;
import marten.aoe.data.type.UnitSize;
import marten.aoe.data.type.UnitType;
import marten.aoe.dto.depreciated.DamageDTO;

public class UnitDetails {

    private UnitSize unitSize = UnitSize.MEDIUM;
    private UnitType unitType = UnitType.GROUND;
    private int maxMovementAllowance = 4;
    private int maxHitPoints = 10;
    private DamageDTO meleeDamage = new DamageDTO(5, DamageType.MECHANICAL);
    private DamageDTO rangedDamage;
    private int attackRange;
    private int detectionRange= 4;
    private int defence = 1;
    public int getDefence() {
        return defence;
    }


    public void setDefence(int defence) {
        this.defence = defence;
    }

    private String id;


    public UnitDetails(String id) {
        this.id = id;
    }


    public UnitDetails(String id, UnitType type) {
        this(id);
        this.unitType = type;
    }

    public UnitSize getUnitSize() {
        return unitSize;
    }

    public void setUnitSize(UnitSize unitSize) {
        this.unitSize = unitSize;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public int getMaxMovementAllowance() {
        return maxMovementAllowance;
    }

    public void setMaxMovementAllowance(int maxMovementAllowance) {
        this.maxMovementAllowance = maxMovementAllowance;
    }

    public int getMaxHitPoints() {
        return maxHitPoints;
    }

    public void setMaxHitPoints(int maxHitPoints) {
        this.maxHitPoints = maxHitPoints;
    }

    public DamageDTO getMeleeDamage() {
        return meleeDamage;
    }

    public void setMeleeDamage(DamageDTO meleeDamage) {
        this.meleeDamage = meleeDamage;
    }

    public DamageDTO getRangedDamage() {
        return rangedDamage;
    }

    public void setRangedDamage(DamageDTO rangedDamage) {
        this.rangedDamage = rangedDamage;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public int getDetectionRange() {
        return detectionRange;
    }

    public void setDetectionRange(int detectionRange) {
        this.detectionRange = detectionRange;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
}
