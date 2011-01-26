package marten.aoe.proposal.engine;

import marten.aoe.proposal.dto.MapDTO;
import marten.aoe.proposal.dto.MinimalMapDTO;
import marten.aoe.proposal.dto.MinimalTileDTO;
import marten.aoe.proposal.dto.MinimalUnitDTO;
import marten.aoe.proposal.dto.PointDTO;
import marten.aoe.proposal.dto.TileDTO;
import marten.aoe.proposal.dto.UnitDTO;
import marten.aoe.proposal.loader.MapLoader;

public final class Engine {
    private final Map map;
    public Engine (String mapName) {
        this.map = MapLoader.loadMap(mapName);
    }
    public Engine (Map map) {
        this.map = map;
    }
    public MinimalMapDTO getMinimalMapDTO (Player player) {        
        return (this.map != null) ? this.map.getMinimalDTO(player) : null;
    }
    public MapDTO getMapDTO (Player player) {
        return (this.map != null) ? this.map.getDTO(player) : null;
    }
    public MinimalTileDTO getMinimalTileDTO (Player player, PointDTO location) {
        return (this.map != null) ? this.map.getTile(location).getMinimalDTO(player) : null;
    }
    public TileDTO getTileDTO (Player player, PointDTO location) {
        return (this.map != null) ? this.map.getTile(location).getDTO(player) : null;
    }
    public MinimalUnitDTO getMinimalUnitDTO (Player player, PointDTO location) {
        return (this.map != null) ? this.map.getTile(location).getUnit().getMinimalDTO(player) : null;
    }
    public UnitDTO getUnitDTO (Player player, PointDTO location) {
        return (this.map != null) ? this.map.getTile(location).getUnit().getDTO(player) : null;
    }
    public void endTurn () {
        if (this.map != null) {
            this.map.endTurn();
        }
    }
}
