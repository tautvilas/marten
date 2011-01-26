package marten.aoe.proposal.engine;

import marten.aoe.proposal.dto.MinimalTileDTO;
import marten.aoe.proposal.dto.Point;
import marten.aoe.proposal.dto.TileDTO;

public abstract class TileBase extends Tile {
    public TileBase(String name, Map owner, Point coordinates) {
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
        return new MinimalTileDTO(this.getName(), (this.getUnit() != null ? this.getUnit().getMinimalDTO(player) : null));        
    }
}
