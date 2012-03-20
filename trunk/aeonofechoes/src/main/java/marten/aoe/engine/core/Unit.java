package marten.aoe.engine.core;

import java.util.HashSet;
import java.util.Set;

import marten.aoe.data.type.DamageType;
import marten.aoe.data.type.UnitSize;
import marten.aoe.data.type.UnitType;
import marten.aoe.data.units.UnitDetails;
import marten.aoe.data.units.Units;
import marten.aoe.dto.UnitDTO;
import marten.aoe.dto.depreciated.DamageDTO;
import marten.aoe.dto.depreciated.DamageResistanceDTO;

public class Unit {
    private Tile location = null;
    private final Player owner;
    private final Set<Player> playerDetection = new HashSet<Player>();
    private UnitDetails details;
    private int currentMovementAllowance;
    private int currentHitPoints;
    private int id;

    public Unit(int uid, UnitDetails details, Tile location, Player owner) {
        this.id = uid;
        this.owner = owner;
        this.location = location;
        this.details = details;
        this.currentHitPoints = details.getMaxHitPoints();
        this.currentMovementAllowance = details.getMaxMovementAllowance();
        if (location != null) {
            location.insertUnit(this.owner, this);
        }
    }

    public int getId() {
        return this.id;
    }

    public UnitDetails getDetails() {
        return this.details;
    }

    public final Units getName() {
        return this.details.getId();
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
        return this.details.getUnitSize();
    }
    /** @return the type of this unit.*/
    public final UnitType getUnitType() {
        return this.details.getUnitType();
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
        return this.details.getDetectionRange();
    }
    /** Invoke the actions applicable to the end of a turn. */
    public final void turnOver() {
        this.currentMovementAllowance = this.details.getMaxMovementAllowance();
        this.onTurnOver();
    }
    /** Create a minimal Unit Data Transfer Object. */
    public final UnitDTO getDTO(Player player) {
        if (player == Player.SYSTEM || this.isDetected(player)) {
            return new UnitDTO(this.getId(),
                               this.details.getId(),
                               this.owner.getDTO(),
                               this.currentHitPoints,
                               this.details.getMaxHitPoints(),
                               this.currentMovementAllowance,
                               this.details.getMaxMovementAllowance(),
                               this.isCloaked(),
                               this.getUnitType());
        }
        return null;
    }
    /** Find out the remaining movement capacity of the unit*/
    public final int getMovementAllowance () {
        return this.currentMovementAllowance;
    }
    public int getPowerupRange() {
        return this.details.getPowerupRange();
    }
    /** Find out the maximum possible movement capacity of the unit*/
    public final int getMaximumMovementAllowance () {
        return this.details.getMaxMovementAllowance();
    }
    /** Find out the remaining hit points of the unit*/
    public final int getHitPoints () {
        return this.currentHitPoints;
    }
    /** Find out the maximum possible hit points of the unit*/
    public final int getMaximumHitPoints () {
        return this.details.getMaxHitPoints();
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
    public void onTileEntry(Tile tile) {};
    /** Performs an action when this unit exits a tile.
     * @param tile - the tile that was exited.*/
    public void onTileExit(Tile tile) {};
    /** Performs an action at the end of every turn.*/
    public void onTurnOver() {};
    /** @return all multipliers for post-armor damage.*/
    public DamageResistanceDTO getDamageResistance() {return null;};
    /** @return a multiplier for post-armor damage of a certain type.*/
    public final int getDamageResistance(DamageType damageType) {
        return this.getDamageResistance().getDamageResistance(damageType);
    }
    /** Applies damage to the unit.*/
    public final void applyDamage(DamageDTO damage) {
        int rolledDamage = damage.getMaxDamage();
        if (rolledDamage > 0) {
            this.currentHitPoints -= rolledDamage;
        }
        if (this.currentHitPoints <= 0) {
            this.getLocation().removeUnit(Player.SYSTEM);
            this.onDeath();
        }
    }
    /** It is invoked whenever the health of the unit reaches 0 or below*/
    public void onDeath() {};
    /** @return brief descriptions of special features of the unit*/
    public String[] getSpecialFeatures() {return new String[0];};
    /***/
    public  boolean isObserving() {return false;};
    /***/
    public  boolean isCloaked() {return false;};
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