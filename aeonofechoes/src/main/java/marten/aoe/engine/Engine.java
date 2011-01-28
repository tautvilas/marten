package marten.aoe.engine;

import marten.aoe.dto.FullMapDTO;
import marten.aoe.dto.MapDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.dto.UnitDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.FullTileDTO;
import marten.aoe.dto.FullUnitDTO;
import marten.aoe.engine.loader.MapLoader;

public final class Engine {
    private final Map map;
    public Engine (String mapName) {
        this.map = MapLoader.loadMap(mapName);
    }
    public Engine (Map map) {
        this.map = map;
    }
    public MapDTO getMinimalMapDTO (Player player) {        
        return (this.map != null) ? this.map.getMinimalDTO(player) : null;
    }
    public FullMapDTO getMapDTO (Player player) {
        return (this.map != null) ? this.map.getDTO(player) : null;
    }
    public TileDTO getMinimalTileDTO (Player player, PointDTO location) {
        return (this.map != null) ? this.map.getTile(location).getMinimalDTO(player) : null;
    }
    public FullTileDTO getTileDTO (Player player, PointDTO location) {
        return (this.map != null) ? this.map.getTile(location).getDTO(player) : null;
    }
    public UnitDTO getMinimalUnitDTO (Player player, PointDTO location) {
        return (this.map != null) ? this.map.getTile(location).getUnit().getMinimalDTO(player) : null;
    }
    public FullUnitDTO getUnitDTO (Player player, PointDTO location) {
        return (this.map != null) ? this.map.getTile(location).getUnit().getDTO(player) : null;
    }
    public void endTurn () {
        if (this.map != null) {
            this.map.endTurn();
        }
    }
}
