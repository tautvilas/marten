package marten.aoe.proposal.engine;

import marten.aoe.proposal.dto.MapDTO;
import marten.aoe.proposal.dto.MinimalMapDTO;
import marten.aoe.proposal.dto.MinimalTileDTO;
import marten.aoe.proposal.dto.MinimalUnitDTO;
import marten.aoe.proposal.dto.Point;
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
    public MinimalMapDTO getMinimalMapDTO () {        
        return (this.map != null) ? this.map.getMinimalDTO() : null;
    }
    public MapDTO getMapDTO () {
        return (this.map != null) ? this.map.getDTO() : null;
    }
    public MinimalTileDTO getMinimalTileDTO (Point location) {
        return (this.map != null) ? this.map.getTile(location).getMinimalDTO() : null;
    }
    public TileDTO getTileDTO (Point location) {
        return (this.map != null) ? this.map.getTile(location).getDTO() : null;
    }
    public MinimalUnitDTO getMinimalUnitDTO (Point location) {
        return (this.map != null) ? this.map.getTile(location).getUnit().getMinimalDTO() : null;
    }
    public UnitDTO getUnitDTO (Point location) {
        return (this.map != null) ? this.map.getTile(location).getUnit().getDTO() : null;
    }
    public void endTurn () {
        if (this.map != null) {
            this.map.endTurn();
        }
    }
}
