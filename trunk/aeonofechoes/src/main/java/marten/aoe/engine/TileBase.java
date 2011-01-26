package marten.aoe.engine;

import marten.aoe.dto.MinimalTileDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;

public abstract class TileBase extends Tile {
    public TileBase(String name, Map owner, PointDTO coordinates) {
        super(name, owner, coordinates);
    }
    @Override public final TileDTO getDTO(Player player) {
        return new TileDTO(
                this.getName(),
                this.getCoordinates(),
                this.getHeight(),
                this.getMovementCost(),
                this.getDefenseBonus(),
                (this.getUnit() != null ? this.getUnit().getDTO(player) : null),
                this.getSpecialFeatures()
        );
    }
    @Override public final MinimalTileDTO getMinimalDTO(Player player) {
        return new MinimalTileDTO(this.getName(), this.getCoordinates(), (this.getUnit() != null ? this.getUnit().getMinimalDTO(player) : null));        
    }
}
