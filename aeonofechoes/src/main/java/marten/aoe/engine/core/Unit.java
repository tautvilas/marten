package marten.aoe.engine.core;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import marten.aoe.data.type.DamageType;
import marten.aoe.data.type.UnitSize;
import marten.aoe.data.type.UnitType;
import marten.aoe.dto.UnitDTO;
import marten.aoe.dto.depreciated.DamageDTO;
import marten.aoe.dto.depreciated.DamageResistanceDTO;

public abstract class Unit {
    private final UnitSize unitSize;
    private final UnitType unitType;
    private Tile location = null;
    private final Player owner;
    private final String name;
    private final int maxMovementAllowance;
    private int currentMovementAllowance;
    private final int maxHitPoints;
    private int currentHitPoints;
    private final int detectionRange;
    private final Set<Player> playerDetection = new HashSet<Player>();

    public Unit (Unit other, Tile location) {
        this.name = other.name;
        this.location = location;
        this.unitSize = other.unitSize;
        this.unitType = other.unitType;
        this.owner = other.owner;
        this.maxMovementAllowance = this.currentMovementAllowance = other.maxMovementAllowance;
        this.maxHitPoints = this.currentHitPoints = other.maxHitPoints;
        this.detectionRange = other.detectionRange;
        if (location != null) {
            location.insertUnit(this.owner, this);
        }
    }
    public Unit(String name, Tile location, Player owner, UnitSize unitSize, UnitType unitType, int movementAllowance, int hitPoints, int detectionRange, int detectionModifier) {
        this.name = name;
        this.location = location;
        this.unitSize = unitSize;
        this.unitType = unitType;
        this.owner = owner;
        this.maxMovementAllowance = this.currentMovementAllowance = movementAllowance;
        this.maxHitPoints = this.currentHitPoints = hitPoints;
        this.detectionRange = detectionRange;
        if (location != null) {
            location.insertUnit(this.owner, this);
        }
    }
    /** @return the name of this unit */
    public final String getName() {
        return this.name;
    }
    /** @return the map, where this unit is located.*/
    public final Map getMap() {
        return this.location.getMap();
    }
    /** @return the owner of this unit.*/
    public final Player getOwner() {
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
    /** Invoke the actions applicable to the end of a turn. */
    public final void turnOver() {
        this.currentMovementAllowance = this.maxMovementAllowance;
        this.onTurnOver();
    }
    /** Create a minimal Unit Data Transfer Object. */
    public final UnitDTO getDTO(Player player) {
        if (player == Player.SYSTEM || this.isDetected(player)) {
            return new UnitDTO(this.name, this.owner.getDTO(), this.currentHitPoints, this.maxHitPoints, this.currentMovementAllowance, this.maxMovementAllowance, this.isCloaked(), this.getUnitType());
        }
        return null;
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
        if (cost <= 0 || cost > this.currentMovementAllowance) {
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
            this.getLocation().removeUnit(Player.SYSTEM);
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
    public abstract boolean isObserving();
    /***/
    public abstract boolean isCloaked();
    public final void setDetected(Player player) {
        this.playerDetection.add(player);
    }
    public final void setUndetected(Player player) {
        this.playerDetection.remove(player);
    }
    public final boolean isDetected(Player player) {
        return this.playerDetection.contains(player);
    }
}