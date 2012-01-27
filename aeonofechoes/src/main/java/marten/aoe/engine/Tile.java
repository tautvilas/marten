package marten.aoe.engine;

import java.util.ArrayList;
import java.util.List;

import marten.aoe.dto.DamageDTO;
import marten.aoe.dto.DefenseDTO;
import marten.aoe.dto.Direction;
import marten.aoe.dto.MovementDTO;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.dto.TileLayerDTO;
import marten.aoe.dto.UnitSize;
import marten.aoe.dto.UnitType;

public abstract class Tile {

    private final Map map;
    private final PointDTO coordinates;
    private ArrayList<TileLayerDTO> layers = new ArrayList<TileLayerDTO>();

    public Tile(Map map, PointDTO coordinates) {
        this.map = map;
        this.coordinates = coordinates;
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
    public abstract Unit getUnit();
    /** @return <code>true</code> if there is a unit in this tile, <code>false</code> otherwise.*/
    public final boolean isOccupied() {
        return this.getUnit() != null;
    }
    /** @param player - the player from whose perspective the data should be presented. Generally this means that data, invisible to this player, is concealed.
     * @return a minimal Tile Data Transfer Object for this tile.
     * @see marten.aoe.engine.Tile#getDTOConcealUnit(PlayerDTO)*/
    public abstract TileDTO getDTO(PlayerDTO player);
    /** Performs an action when a unit enters this tile.*/
    public abstract void onUnitEntry();
    /** Performs an action when a unit leaves this tile.*/
    public abstract void onUnitExit();
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
    public ArrayList<TileLayerDTO> getLayers() {
        return this.layers;
    }
    public void addLayer(TileLayerDTO layer) {
        this.layers.add(layer);
    }
    public abstract boolean isExplored(PlayerDTO player);
    public abstract boolean isVisible(PlayerDTO player);
    public abstract boolean isDetected(PlayerDTO player);
    public abstract boolean hasAnythingCloaked(PlayerDTO player);
    public abstract void setExplored(PlayerDTO player);
    public abstract void setVisible(PlayerDTO player);
    public abstract void setInvisible(PlayerDTO player);
    public abstract void setDetected(PlayerDTO player);
    public abstract void setUndetected(PlayerDTO player);
    public abstract Unit popUnit(PlayerDTO player);
    public abstract boolean pushUnit(PlayerDTO player, Unit unit);
    public abstract Unit removeUnit(PlayerDTO player);
    public abstract boolean insertUnit(PlayerDTO player, Unit unit);
    public abstract void turnOver();
    public abstract void applyDamage(DamageDTO damage);
}
