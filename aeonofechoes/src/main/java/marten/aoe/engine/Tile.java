package marten.aoe.engine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import marten.aoe.GameInfo;
import marten.aoe.dto.DamageDTO;
import marten.aoe.dto.DefenseDTO;
import marten.aoe.dto.Direction;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.dto.TileLayerDTO;
import marten.aoe.dto.UnitSize;
import marten.aoe.dto.UnitType;

public class Tile {
    private Unit unit = null;
    private final Set<PlayerDTO> exploredPlayers = new HashSet<PlayerDTO>();
    private final Set<PlayerDTO> visiblePlayers = new HashSet<PlayerDTO>();
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

    /**
     * @return <code>true</code> if there is a unit in this tile,
     *         <code>false</code> otherwise.
     */
    public final boolean isOccupied() {
        return this.getUnit() != null;
    }

    /**
     * Calculates the defense value of being in this tile.
     * 
     * @param size
     *            - the size of the unit.
     * @param type
     *            - the type of the unit.
     * @return the amount of points the maximum attacking force is reduced by
     *         when the unit is defending in this tile.
     */
    public final int getDefenseBonus(UnitSize size, UnitType type) {
        return this.getDefenseBonus().getValue(size, type);
    }

    public final Tile adjacent(Direction direction) {
        return this.map.getTile(direction.adjust(this.coordinates));
    }

    public final int distanceTo(Tile other) {
        int minimumEstimate = other.coordinates.getX()
                - this.coordinates.getX();
        minimumEstimate *= (minimumEstimate < 0 ? -1 : 1);
        int minY = this.coordinates.getY() - minimumEstimate / 2;
        int maxY = this.coordinates.getY() + minimumEstimate / 2;
        if (minimumEstimate % 2 != 0) {
            if (this.coordinates.getX() % 2 == 0) {
                --minY;
            } else {
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

    public final List<Tile> neighbors(int distance) {
        List<Tile> answer = new ArrayList<Tile>();
        for (int x = this.coordinates.getX() - distance; x <= this.coordinates
                .getX() + distance; ++x) {
            for (int y = this.coordinates.getY() - distance; y <= this.coordinates
                    .getY() + distance; ++y) {
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

    public final TileDTO getDTO(PlayerDTO player) {
        if (player != PlayerDTO.SYSTEM && !this.isExplored(player)) {
            return new TileDTO("Shroud", this.getCoordinates(), null, false);
        }
        ArrayList<String> lnames = new ArrayList<String>();
        for (int i = 0; i < this.getLayers().size(); i++) {
            lnames.add(this.getLayers().get(i).getName());
        }
        String[] arr = new String[lnames.size()];
        return new TileDTO(lnames.toArray(arr), this.getCoordinates(),
                (this.getUnit() != null && this.isVisible(player) ? this
                        .getUnit().getDTO(player) : null),
                this.isVisible(player));
    }

    public final boolean isExplored(PlayerDTO player) {
        return this.exploredPlayers.contains(player);
    }

    public final boolean isVisible(PlayerDTO player) {
        return this.visiblePlayers.contains(player);
    }

    public final void setExplored(PlayerDTO player) {
        this.exploredPlayers.add(player);
    }

    public final void setVisible(PlayerDTO player) {
        this.visiblePlayers.add(player);
    }

    public final void setInvisible(PlayerDTO player) {
        this.visiblePlayers.remove(player);
    }

    public final Unit getUnit() {
        return this.unit;
    }

    public final Unit popUnit(PlayerDTO player) {
        if (this.unit != null
                && (player == this.unit.getOwner() || player == PlayerDTO.SYSTEM)) {
            this.onUnitExit();
            this.unit.onTileExit(this);
            return this.removeUnit(player);
        }
        return null;
    }

    public final boolean pushUnit(PlayerDTO player, Unit unit) {
        if (this.unit == null
                && unit != null
                && (player == unit.getOwner() || player == PlayerDTO.SYSTEM)
                && (unit.applyMovementCost(GameInfo.calculator.getMovementCost(this.getDTO(player), unit.getDTO(player))) > -1)) {
            this.unit = unit;
            this.unit.onTileEntry(this);
            this.unit.setLocation(this);
            this.onUnitEntry();
            return true;
        }
        return false;
    }

    public final Unit removeUnit(PlayerDTO player) {
        if (this.unit != null
                && (player == this.unit.getOwner() || player == PlayerDTO.SYSTEM)) {
            Unit answer = this.unit;
            this.unit = null;
            answer.setLocation(null);
            return answer;
        }
        return null;
    }

    public final boolean insertUnit(PlayerDTO player, Unit unit) {
        if (this.unit == null && unit != null
                && (player == unit.getOwner() || player == PlayerDTO.SYSTEM)) {
            this.unit = unit;
            this.unit.setLocation(this);
            return true;
        }
        return false;
    }

    public final void applyDamage(DamageDTO damage) {
        if (this.unit != null) {
            this.unit.applyDamage(damage);
        }
    }

    public final void turnOver() {
        if (this.unit != null) {
            this.unit.turnOver();
        }
    }

    public final boolean isDetected(PlayerDTO player) {
        return (this.unit != null ? this.unit.isDetected(player) : true);
    }

    public final void setDetected(PlayerDTO player) {
        if (this.unit != null) {
            this.unit.setDetected(player);
        }
    }

    public final void setUndetected(PlayerDTO player) {
        if (this.unit != null) {
            this.unit.setUndetected(player);
        }
    }

    public final boolean hasAnythingCloaked(PlayerDTO player) {
        if (this.unit == null) {
            return false;
        }
        return this.unit.isCloaked();
    }

    public void onUnitEntry() {
        // TODO Auto-generated method stub

    }

    public void onUnitExit() {
        // TODO Auto-generated method stub

    }

    public DefenseDTO getDefenseBonus() {
        // TODO Auto-generated method stub
        return null;
    }

    public int getHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    public String[] getSpecialFeatures() {
        // TODO Auto-generated method stub
        return null;
    }
}
