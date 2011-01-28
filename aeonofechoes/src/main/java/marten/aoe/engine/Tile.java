package marten.aoe.engine;

import java.util.ArrayList;
import java.util.List;

import marten.aoe.dto.DamageDTO;
import marten.aoe.dto.DefenseDTO;
import marten.aoe.dto.Direction;
import marten.aoe.dto.TileDTO;
import marten.aoe.dto.MovementDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.FullTileDTO;
import marten.aoe.dto.UnitSize;
import marten.aoe.dto.UnitType;

public abstract class Tile {
    
    private Unit unit = null;
    private final Map owner;
    private final PointDTO coordinates;
    private final String name;
    private TileLayer overlay;
    
    public Tile(String name, Map owner, PointDTO coordinates) {
        this.name = name;
        this.owner = owner;
        this.coordinates = coordinates;
    }
    public String getName() {
        return this.name;
    }
    /** @return the owner of this tile. */
    public final Map getOwner() {
        return this.owner;        
    }
    /** @return the coordinates of this tile. */
    public final PointDTO getCoordinates() {
        return this.coordinates;
    }
    /** @return the unit in this tile or <code>null</code> if there is no unit. */
    public final Unit getUnit() {
        return this.unit;
    }
    /** @return <code>true</code> if there is a unit in this tile, <code>false</code> otherwise.*/
    public final boolean isOccupied() {
        return this.unit != null;
    }
    /** Returns a standard Tile Data Transfer Object for this tile.*/
    public abstract FullTileDTO getDTO(Player player);
    /** Returns a minimal Tile Data Transfer Object for this tile.*/
    public abstract TileDTO getMinimalDTO(Player player);
    
    /** Removes the unit from this tile and triggers appropriate events.
     * @return the unit formerly in this tile or <code>null</code> if there was no unit. 
     * @see marten.aoe.engine.Tile#removeUnit()*/
    public final Unit popUnit(Player player) {
        if (player == this.unit.getOwner() || player == Player.SYSTEM) {
            this.onUnitExit();
            this.unit.onTileExit(this);
            return this.removeUnit(player);
        }
        return null;
    }
    /** Insert a unit into this tile, triggering appropriate events and applying movement cost. 
     * @return <code>false</code> if the action failed due to a unit already being in this tile, unit having insufficient movement allowance or no unit being pushed in, <code>true</code> otherwise. 
     * @see marten.aoe.engine.Tile#insertUnit(Unit)*/
    public final boolean pushUnit(Player player, Unit unit) {
        if ((player == this.unit.getOwner() || player == Player.SYSTEM) && this.unit == null && unit != null && (unit.applyMovementCost(this.getMovementCost(unit.getUnitSize(), unit.getUnitType())) > -1)) {
            this.unit = unit;
            this.unit.onTileEntry(this);
            this.onUnitEntry();
            return true;
        }
        return false;
    }
    /** Removes the unit from this tile without triggering events.
     * @return the unit in this tile (and removes it from the tile) or <code>null</code>.
     * @see marten.aoe.engine.Tile#popUnit()*/
    public final Unit removeUnit(Player player) {
        if (player == this.unit.getOwner() || player == Player.SYSTEM) {
            Unit answer = this.unit;
            this.unit = null;
            return answer;
        }
        return null;
    }
    /** Insert a unit into this tile, without triggering appropriate events and applying movement cost. 
     * @return <code>false</code> if the action failed due to a unit already being in this tile, unit having insufficient movement allowance or no unit being pushed in, <code>true</code> otherwise. 
     * @see marten.aoe.engine.Tile#pushUnit(Unit)*/
    public final boolean insertUnit(Player player, Unit unit) {
        if ((player == this.unit.getOwner() || player == Player.SYSTEM) && this.unit == null && unit != null) {
            this.unit = unit;
            return true;
        }
        return false;
    }
    public final void turnOver() {
        if (this.unit != null) {
            this.unit.turnOver();
        }
        this.onTurnOver();
    }
    /** Performs an action when a unit enters this tile.*/
    public abstract void onUnitEntry();
    /** Performs an action when a unit leaves this tile.*/
    public abstract void onUnitExit();
    /** Performs an action at the end of every turn.*/
    public abstract void onTurnOver();
    /** Calculates all movement costs and returns it as a DTO.*/
    public abstract MovementDTO getMovementCost();
    /** Calculates the movement cost of entering this tile. */
    public final int getMovementCost(UnitSize size, UnitType type) {
        return this.getMovementCost().getValue(size, type);
    }
    /** Calculates all defense bonuses and returns it as a DTO.*/
    public abstract DefenseDTO getDefenseBonus();
    /** Calculates the defense value of being in this tile. */
    public final int getDefenseBonus(UnitSize size, UnitType type) {
        return this.getDefenseBonus().getValue(size, type);
    }
    /** Calculates the height at which this tile currently is. */
    public abstract int getHeight();
    /** Returns a description of the special features of this tile.*/
    public abstract String[] getSpecialFeatures(); 
    public final void applyDamage(DamageDTO damage) {
        if (this.unit != null) {
            this.unit.applyDamage(damage);
        }
    }
    public final TileLayer getOverlay() {
        return this.overlay;
    }
    public final void setOverlay(TileLayer overlay) {
        this.overlay = overlay;
    }
    public final Tile adjacent (Direction direction) {
        return owner.getTile(direction.adjust(this.coordinates));
    }
    public final int distanceTo (Tile other) {
        int minimumEstimate = other.coordinates.getX() - this.coordinates.getX();
        minimumEstimate *= (minimumEstimate < 0 ? -1 : 1);
        int minY = this.coordinates.getY() - minimumEstimate / 2;
        int maxY = this.coordinates.getY() + minimumEstimate / 2;
        if (minimumEstimate % 2 != 0) {
            if (this.coordinates.getX() % 2 == 0) {
                --minY;
            }
            else {
                ++maxY;
            }
        }
        if (other.coordinates.getY() > maxY) {
            return minimumEstimate + other.coordinates.getY() - maxY;
        }
        if (other.coordinates.getY() < minY) {
            return minimumEstimate + minY - other.coordinates.getY();
        }
        return minimumEstimate;
    }
    public final List<Tile> neighbors (int distance) {
        List<Tile> answer = new ArrayList<Tile>();
        for (int x = this.coordinates.getX() - distance; x <= this.coordinates.getX() + distance; ++x) {
            for (int y = this.coordinates.getY() - distance; y <= this.coordinates.getY() + distance; ++y) {
                Tile candidate = this.owner.getTile(new PointDTO(x, y));
                if (this.distanceTo(candidate) <= distance) {
                    answer.add(candidate);
                }
            }
        }
        return answer;
    }
}
