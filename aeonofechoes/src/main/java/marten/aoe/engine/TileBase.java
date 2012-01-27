package marten.aoe.engine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import marten.aoe.dto.DamageDTO;
import marten.aoe.dto.DefenseDTO;
import marten.aoe.dto.MovementDTO;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;

public class TileBase extends Tile {
    private Unit unit = null;
    private final Set<PlayerDTO> exploredPlayers = new HashSet<PlayerDTO>();
    private final Set<PlayerDTO> visiblePlayers = new HashSet<PlayerDTO>();

    public TileBase(Map owner, PointDTO coordinates) {
        super(owner, coordinates);
    }

    @Override
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
                        .getUnit().getDTO(player) : null), this
                        .isVisible(player));
    }

    @Override
    public final boolean isExplored(PlayerDTO player) {
        return this.exploredPlayers.contains(player);
    }

    @Override
    public final boolean isVisible(PlayerDTO player) {
        return this.visiblePlayers.contains(player);
    }

    @Override
    public final void setExplored(PlayerDTO player) {
        this.exploredPlayers.add(player);
    }

    @Override
    public final void setVisible(PlayerDTO player) {
        this.visiblePlayers.add(player);
    }

    @Override
    public final void setInvisible(PlayerDTO player) {
        this.visiblePlayers.remove(player);
    }

    @Override
    public final Unit getUnit() {
        return this.unit;
    }

    @Override
    public final Unit popUnit(PlayerDTO player) {
        if (this.unit != null
                && (player == this.unit.getOwner() || player == PlayerDTO.SYSTEM)) {
            this.onUnitExit();
            this.unit.onTileExit(this);
            return this.removeUnit(player);
        }
        return null;
    }

    @Override
    public final boolean pushUnit(PlayerDTO player, Unit unit) {
        if (this.unit == null
                && unit != null
                && (player == unit.getOwner() || player == PlayerDTO.SYSTEM)
                && (unit.applyMovementCost(this.getMovementCost(unit
                        .getUnitSize(), unit.getUnitType())) > -1)) {
            this.unit = unit;
            this.unit.onTileEntry(this);
            this.unit.setLocation(this);
            this.onUnitEntry();
            return true;
        }
        return false;
    }

    @Override
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

    @Override
    public final boolean insertUnit(PlayerDTO player, Unit unit) {
        if (this.unit == null && unit != null
                && (player == unit.getOwner() || player == PlayerDTO.SYSTEM)) {
            this.unit = unit;
            this.unit.setLocation(this);
            return true;
        }
        return false;
    }

    @Override
    public final void applyDamage(DamageDTO damage) {
        if (this.unit != null) {
            this.unit.applyDamage(damage);
        }
    }

    @Override
    public final void turnOver() {
        if (this.unit != null) {
            this.unit.turnOver();
        }
    }

    @Override
    public final boolean isDetected(PlayerDTO player) {
        return (this.unit != null ? this.unit.isDetected(player) : true);
    }

    @Override
    public final void setDetected(PlayerDTO player) {
        if (this.unit != null) {
            this.unit.setDetected(player);
        }
    }

    @Override
    public final void setUndetected(PlayerDTO player) {
        if (this.unit != null) {
            this.unit.setUndetected(player);
        }
    }

    @Override
    public final boolean hasAnythingCloaked(PlayerDTO player) {
        if (this.unit == null) {
            return false;
        }
        return this.unit.isCloaked();
    }
    
    @Override
    public void onUnitEntry() {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void onUnitExit() {
        // TODO Auto-generated method stub
        
    }
    @Override
    public MovementDTO getMovementCost() {
        return new MovementDTO(this.getLayers().get(0).getGroundMovementCost());
    }
    @Override
    public DefenseDTO getDefenseBonus() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public int getHeight() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public String[] getSpecialFeatures() {
        // TODO Auto-generated method stub
        return null;
    }
}
