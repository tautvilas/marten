package marten.aoe.proposal.engine;

import marten.aoe.proposal.dto.UnitDTO;
import marten.aoe.proposal.dto.UnitSize;
import marten.aoe.proposal.dto.UnitType;

public abstract class Unit {
    private final Map map;
    private final UnitSize unitSize;
    private final UnitType unitType;
    private Tile location = null;
    private final Player owner;
    
    public Unit(Map map, Player owner, UnitSize unitSize, UnitType unitType) {
        this.map = map;
        this.unitSize = unitSize;
        this.unitType = unitType;
        this.owner = owner;
    }
    /** @return the map, where this unit is located.*/
    public final Map getMap() {
        return this.map;
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
    /** Create a standard Unit Data Transfer Object. */
    public final UnitDTO getDTO() {
        // FIXME what shall we use to fill the empty spaces where we used to talk?
        return null;
    }
    /** Apply the movement cost of entering a tile.
     * @param cost - the integer cost of entering a tile.
     * @return <code>-1</code> if movement was impossible to complete for any reason, remaining movement points otherwise.*/
    public abstract int applyMovementCost(int cost);
    /** Performs an action when this unit enters a tile.
     * @param tile - the tile that was entered.*/
    public abstract void onTileEntry(Tile tile);
    /** Performs an action when this unit exits a tile.
     * @param tile - the tile that was exited.*/
    public abstract void onTileExit(Tile tile);
    /** Performs an action at the end of every turn.*/
    public abstract void onTurnOver();
}
