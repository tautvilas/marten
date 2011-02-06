package marten.aoe.engine;

import java.util.Random;

import marten.aoe.dto.DamageDTO;
import marten.aoe.dto.DamageResistanceDTO;
import marten.aoe.dto.DamageType;
import marten.aoe.dto.FullUnitDTO;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.UnitDTO;
import marten.aoe.dto.UnitSize;
import marten.aoe.dto.UnitType;

public abstract class Unit {
    private final UnitSize unitSize;
    private final UnitType unitType;
    private Tile location = null;
    private final PlayerDTO owner;
    private final String name;
    private final int maxMovementAllowance;
    private int currentMovementAllowance;
    private final int maxHitPoints;
    private int currentHitPoints;
    private final int detectionRange;
    private final int detectionModifier;

    public Unit (Unit other, Tile location) {
        this.name = other.name;
        this.location = location;
        this.unitSize = other.unitSize;
        this.unitType = other.unitType;
        this.owner = other.owner;
        this.maxMovementAllowance = this.currentMovementAllowance = other.maxMovementAllowance;
        this.maxHitPoints = this.currentHitPoints = other.maxHitPoints;
        this.detectionRange = other.detectionRange;
        this.detectionModifier = other.detectionModifier;
    }
    public Unit(String name, Tile location, PlayerDTO owner, UnitSize unitSize, UnitType unitType, int movementAllowance, int hitPoints, int detectionRange, int detectionModifier) {
        this.name = name;
        this.location = location;
        this.unitSize = unitSize;
        this.unitType = unitType;
        this.owner = owner;
        this.maxMovementAllowance = this.currentMovementAllowance = movementAllowance;
        this.maxHitPoints = this.currentHitPoints = hitPoints;
        this.detectionRange = detectionRange;
        this.detectionModifier = detectionModifier;
    }
    /** @return the name of this unit */
    public final String getName() {
        return this.name;
    }
    /** @return the map, where this unit is located.*/
    public final Map getMap() {
        return this.location.getOwner();
    }
    /** @return the owner of this unit.*/
    public final PlayerDTO getOwner() {
        return this.owner;
    }
    /** @return the size of this unit.*/
    public final UnitSize getUnitSize() {
        return this.unitSize;
    }
    /** @return the type of this unit.*/
    public final UnitType getUnitType() {
        return this.unitType;
    }
    /** Set the current location of the unit. Use <code>null</code> to show it is not on the map.*/
    public final void setLocation(Tile location) {
        this.location = location;
    }
    /** @return the current location of the unit or <code>null</code> if it is not on the map.*/
    public final Tile getLocation() {
        return this.location;
    }
    /** @return the unmodified range, where the unit is capable of seeing enemy units.*/
    public final int getDetectionRange() {
        return this.detectionRange;
    }
    /** @return the range modifier for enemies attempting detection. More negative is better.*/
    public final int getDetectionModifier() {
        return this.detectionModifier;
    }
    /** Create a standard Unit Data Transfer Object. */
    public final FullUnitDTO getDTO(PlayerDTO player) {
        for (Unit unit : this.getMap().getAllUnits(player)) {
            if (this.getLocation().distanceTo(unit.getLocation()) + this.detectionModifier <= 0) {
                return new FullUnitDTO(
                        this.name,
                        this.unitSize,
                        this.unitType,
                        this.currentMovementAllowance,
                        this.maxMovementAllowance,
                        this.getDamageResistance(),
                        this.currentHitPoints,
                        this.maxHitPoints,
                        this.detectionRange,
                        this.getSpecialFeatures()
                );
            }
        }
        return null;
    }
    /** Invoke the actions applicable to the end of a turn. */
    public void turnOver() {
        this.currentMovementAllowance = this.maxMovementAllowance;
        this.onTurnOver();
    }
    /** Create a minimal Unit Data Transfer Object. */
    public final UnitDTO getMinimalDTO(PlayerDTO player) {
        return new UnitDTO(this.name);
    }
    /** Find out the remaining movement capacity of the unit*/
    public final int getMovementAllowance () {
        return this.currentMovementAllowance;
    }
    /** Find out the maximum possible movement capacity of the unit*/
    public final int getMaximumMovementAllowance () {
        return this.maxMovementAllowance;
    }
    /** Find out the remaining hit points of the unit*/
    public final int getHitPoints () {
        return this.currentHitPoints;
    }
    /** Find out the maximum possible hit points of the unit*/
    public final int getMaximumHitPoints () {
        return this.maxHitPoints;
    }
    /** Apply the movement cost of entering a tile.
     * @param cost - the integer cost of entering a tile.
     * @return <code>-1</code> if movement was impossible to complete for any reason, remaining movement points otherwise.*/
    public final int applyMovementCost(int cost) {
        if (cost > this.currentMovementAllowance) {
            return -1;
        }
        this.currentMovementAllowance -= cost;
        return this.currentMovementAllowance;
    }
    /** Performs an action when this unit enters a tile.
     * @param tile - the tile that was entered.*/
    public abstract void onTileEntry(Tile tile);
    /** Performs an action when this unit exits a tile.
     * @param tile - the tile that was exited.*/
    public abstract void onTileExit(Tile tile);
    /** Performs an action at the end of every turn.*/
    public abstract void onTurnOver();
    /** @return all multipliers for post-armor damage.*/
    public abstract DamageResistanceDTO getDamageResistance();
    /** @return a multiplier for post-armor damage of a certain type.*/
    public final int getDamageResistance(DamageType damageType) {
        return this.getDamageResistance().getDamageResistance(damageType);
    }
    /** Applies damage to the unit.*/
    public final void applyDamage(DamageDTO damage) {
        Random random = new Random();
        int rolledDamage = random.nextInt(damage.getMaxDamage()) + 1;
        rolledDamage = rolledDamage + this.getDamageResistance(damage.getDamageType()) + this.getLocation().getDefenseBonus(this.unitSize, this.unitType);
        if (rolledDamage > 0) {
            this.currentHitPoints -= rolledDamage;
        }
        if (this.currentHitPoints <= 0) {
            this.getLocation().removeUnit(PlayerDTO.SYSTEM);
            this.onDeath();
        }
    }
    /** It is invoked whenever the health of the unit reaches 0 or below*/
    public abstract void onDeath();
    /** @return brief descriptions of special features of the unit*/
    public abstract String[] getSpecialFeatures();
    /** @return a perfect copy of the unit in given location*/
    public abstract Unit clone(Tile location);
    /***/
    public abstract void specialAction1(PointDTO target);
    public abstract void specialAction2(PointDTO target);
    public abstract void specialAction3(PointDTO target);
    public abstract void specialAction4(PointDTO target);
    public abstract void specialAction5(PointDTO target);
    public abstract void specialAction6(PointDTO target);
    public abstract void specialAction7(PointDTO target);
    public abstract void specialAction8(PointDTO target);
    public abstract void specialAction9(PointDTO target);
}