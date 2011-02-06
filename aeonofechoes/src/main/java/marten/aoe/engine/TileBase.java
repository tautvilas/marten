package marten.aoe.engine;

import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.FullTileDTO;

public abstract class TileBase extends Tile {
    public TileBase(String name, Map owner, PointDTO coordinates) {
        super(name, owner, coordinates);
    }
    @Override public final FullTileDTO getDTO(PlayerDTO player) {
        return new FullTileDTO(
                this.getName(),
                this.getCoordinates(),
                this.getHeight(),
                this.getMovementCost(),
                this.getDefenseBonus(),
                (this.getUnit() != null ? this.getUnit().getDTO(player) : null),
                this.getSpecialFeatures()
        );
    }
    @Override public final TileDTO getMinimalDTO(PlayerDTO player) {
        return new TileDTO(this.getName(), this.getCoordinates(), (this.getUnit() != null ? this.getUnit().getMinimalDTO(player) : null));        
    }
}
