package marten.aoe.engine;

import java.util.HashSet;
import java.util.Set;

import marten.aoe.dto.DefenseDTO;
import marten.aoe.dto.FullTileDTO;
import marten.aoe.dto.MovementDTO;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;

public abstract class TileBase extends Tile {
    private final Set<PlayerDTO> exploredPlayers = new HashSet<PlayerDTO>();
    public TileBase(String name, Map owner, PointDTO coordinates) {
        super(name, owner, coordinates);
    }
    @Override public final FullTileDTO getFullDTO(PlayerDTO player) {
        if (!this.isExplored(player)) {
            return new FullTileDTO(
                    "Shroud",
                    this.getCoordinates(),
                    0,
                    new MovementDTO(null),
                    new DefenseDTO(null),
                    null,
                    new String[] {"Unexplored"},
                    false
            );
        }
        return new FullTileDTO(
                this.getName(),
                this.getCoordinates(),
                this.getHeight(),
                this.getMovementCost(),
                this.getDefenseBonus(),
                (this.getUnit() != null && this.isVisible(player) ? this.getUnit().getFullDTO(player) : null),
                this.getSpecialFeatures(),
                this.isVisible(player)
        );
    }
    @Override public final TileDTO getDTO(PlayerDTO player) {
        if (!this.isExplored(player)) {
            return new TileDTO("Shroud", this.getCoordinates(), null, false);
        }
        return new TileDTO(this.getName(), this.getCoordinates(), (this.getUnit() != null && this.isVisible(player) ? this.getUnit().getDTO(player) : null), this.isVisible(player));
    }
    @Override public final void markAsExplored(PlayerDTO player) {
        this.exploredPlayers.add(player);
        this.getMap().invokeLocalEvent(LocalEvent.TILE_EXPLORED, this.getCoordinates());
    }
    @Override public final boolean isExplored(PlayerDTO player) {
        return this.exploredPlayers.contains(player);
    }
    @Override public final boolean isVisible(PlayerDTO player) {
        for (Unit unit : this.getMap().getAllUnits(player)) {
            if (this.distanceTo(unit.getLocation()) <= unit.getDetectionRange()) {
                return true;
            }
        }
        return false;
    }
}
