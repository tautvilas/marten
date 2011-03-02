package marten.aoe.engine;

import java.util.ArrayList;
import java.util.List;

import marten.aoe.dto.DamageDTO;
import marten.aoe.dto.DefenseDTO;
import marten.aoe.dto.Direction;
import marten.aoe.dto.FullTileDTO;
import marten.aoe.dto.MovementDTO;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.dto.UnitSize;
import marten.aoe.dto.UnitType;

public abstract class Tile {

    private Unit unit = null;
    private final Map map;
    private final PointDTO coordinates;
    private final String name;
    private TileLayer overlay;

    public Tile(String name, Map map, PointDTO coordinates) {
        this.name = name;
        this.map = map;
        this.coordinates = coordinates;
    }
    public String getName() {
        return this.name;
    }
    /** @return the owner of this tile. */
    public final Map getMap() {
        return this.map;
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
    /** @param player - the player from whose perspective the data should be presented. Generally this means that data, invisible to this player, is concealed.
     * @return a standard Tile Data Transfer Object for this tile.*/
    public abstract FullTileDTO getFullDTO(PlayerDTO player);
    /** @param player - the player from whose perspective the data should be presented. Generally this means that data, invisible to this player, is concealed.
     * @return a minimal Tile Data Transfer Object for this tile.
     * @see marten.aoe.engine.Tile#getDTOConcealUnit(PlayerDTO)*/
    public abstract TileDTO getDTO(PlayerDTO player);
    /** This getter should be used instead of {@link #getDTO(PlayerDTO)} whenever the unit is about to be removed from the map and is needed only for visibility purposes, e.g., firing events.
     * @param player - the player from whose perspective the data should be presented. Generally this means that data, invisible to this player, is concealed.
     * @return a minimal Tile Data Transfer Object with nullified unit data.
     * @see marten.aoe.engine.Tile#getDTO(PlayerDTO)*/
    public final TileDTO getDTOConcealUnit(PlayerDTO player) {
        TileDTO fullDTO = this.getDTO(player);
        return new TileDTO (fullDTO.getName(), fullDTO.getCoordinates(), null, fullDTO.getVisibility());
    }
    /** Removes the unit from this tile and triggers appropriate events.
     * @param player - the player who is performing the action.
     * @return <code>null</code> if the player is unauthorized to perform this action or there is no unit to be removed, the removed unit otherwise.
     * @see marten.aoe.engine.Tile#removeUnit()*/
    public final Unit popUnit(PlayerDTO player) {
        if (this.unit != null && (player == this.unit.getOwner() || player == PlayerDTO.SYSTEM)) {
            this.onUnitExit();
            this.unit.onTileExit(this);
            return this.removeUnit(player);
        }
        return null;
    }
    /** Insert a unit into this tile, triggering appropriate events and applying movement cost.
     * @param player - the player who is performing the action.
     * @param unit - the unit which is being pushed into the tile.
     * @return <code>false</code> if the action failed due to player being unauthorized to perform this action, a unit already being in this tile, unit having insufficient movement allowance or no unit being pushed in, <code>true</code> otherwise.
     * @see marten.aoe.engine.Tile#insertUnit(Unit)*/
    public final boolean pushUnit(PlayerDTO player, Unit unit) {
        if (this.unit == null && unit != null && (player == unit.getOwner() || player == PlayerDTO.SYSTEM) && (unit.applyMovementCost(this.getMovementCost(unit.getUnitSize(), unit.getUnitType())) > -1)) {
            this.unit = unit;
            this.unit.onTileEntry(this);
            this.unit.setLocation(this);
            this.onUnitEntry();
            this.getMap().invokeLocalEvent(LocalEvent.UNIT_ENTRY, this.getCoordinates());
            for (Tile exploredTile : this.neighbors(unit.getDetectionRange())) {
                exploredTile.recheckVisibility(player);
            }
            return true;
        }
        return false;
    }
    /** Removes the unit from this tile without triggering events.
     * @param player - the player who is performing the action.
     * @return the unit in this tile (and removes it from the tile) or <code>null</code>.
     * @see marten.aoe.engine.Tile#popUnit()*/
    public final Unit removeUnit(PlayerDTO player) {
        if (this.unit != null && (player == this.unit.getOwner() || player == PlayerDTO.SYSTEM)) {
            this.getMap().invokeLocalEventConcealUnit(LocalEvent.UNIT_EXIT, this.getCoordinates());
            Unit answer = this.unit;
            this.unit = null;
            answer.setLocation(null);
            for (Tile exploredTile : this.neighbors(answer.getDetectionRange())) {
                exploredTile.recheckVisibility(player);
            }
            return answer;
        }
        return null;
    }
    /** Insert a unit into this tile, without triggering appropriate events and applying movement cost.
     * @param player - the player who is performing the action.
     * @param unit - the unit which is being pushed into the tile.
     * @return <code>false</code> if the action failed due to a unit already being in this tile, unit having insufficient movement allowance or no unit being pushed in, <code>true</code> otherwise.
     * @see marten.aoe.engine.Tile#pushUnit(Unit)*/
    public final boolean insertUnit(PlayerDTO player, Unit unit) {
        if (this.unit == null && unit != null && (player == unit.getOwner() || player == PlayerDTO.SYSTEM)) {
            this.unit = unit;
            this.unit.setLocation(this);
            this.getMap().invokeLocalEvent(LocalEvent.UNIT_ENTRY, this.getCoordinates());
            for (Tile exploredTile : this.neighbors(unit.getDetectionRange())) {
                exploredTile.recheckVisibility(player);
            }
            return true;
        }
        return false;
    }
    /** Redirects "turn over" message to the unit inside this tile.*/
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
    /** Calculates the movement cost of entering this tile.
     * @param size - the size of the unit.
     * @param type - the type of the unit.
     * @return the amount of movement points to be subtracted if the unit enters this tile. */
    public final int getMovementCost(UnitSize size, UnitType type) {
        return this.getMovementCost().getValue(size, type);
    }
    /** Calculates all defense bonuses and returns it as a DTO.*/
    public abstract DefenseDTO getDefenseBonus();
    /** Calculates the defense value of being in this tile.
     * @param size - the size of the unit.
     * @param type - the type of the unit.
     * @return the amount of points the maximum attacking force is reduced by when the unit is defending in this tile.*/
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
        return this.map.getTile(direction.adjust(this.coordinates));
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
                Tile candidate = this.map.getTile(new PointDTO(x, y));
                if (candidate != null && this.distanceTo(candidate) <= distance) {
                    answer.add(candidate);
                }
            }
        }
        return answer;
    }
    public abstract boolean isExplored(PlayerDTO player);
    public abstract boolean isVisible(PlayerDTO player);
    public abstract void recheckVisibility(PlayerDTO player);
}
