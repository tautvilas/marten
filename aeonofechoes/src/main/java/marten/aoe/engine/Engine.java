package marten.aoe.engine;

import marten.aoe.dto.FullMapDTO;
import marten.aoe.dto.FullTileDTO;
import marten.aoe.dto.FullUnitDTO;
import marten.aoe.dto.MapDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.dto.UnitDTO;
import marten.aoe.engine.loader.MapLoader;

public final class Engine {
    private final Map map;
    public Engine (String mapName) {
        this.map = MapLoader.loadMap(mapName);
    }
    public Engine (Map map) {
        this.map = map;
    }
    public synchronized MapDTO getMinimalMapDTO (Player player) {
        return (this.map != null) ? this.map.getMinimalDTO(player) : null;
    }
    public synchronized FullMapDTO getMapDTO (Player player) {
        return (this.map != null) ? this.map.getDTO(player) : null;
    }
    public synchronized TileDTO getMinimalTileDTO (Player player, PointDTO location) {
        return (this.map != null) ? this.map.getTile(location).getMinimalDTO(player) : null;
    }
    public synchronized FullTileDTO getTileDTO (Player player, PointDTO location) {
        return (this.map != null) ? this.map.getTile(location).getDTO(player) : null;
    }
    public synchronized UnitDTO getMinimalUnitDTO (Player player, PointDTO location) {
        return (this.map != null) ? this.map.getTile(location).getUnit().getMinimalDTO(player) : null;
    }
    public synchronized FullUnitDTO getUnitDTO (Player player, PointDTO location) {
        return (this.map != null) ? this.map.getTile(location).getUnit().getDTO(player) : null;
    }
    public synchronized boolean moveUnit (Player player, PointDTO from, PointDTO to) {
        return this.map.moveUnit(player, from, to);
    }
    @Deprecated public synchronized boolean createUnit (Player player, String name, PointDTO at) {
        // For testing purposes only. In normal circumstances the players should
        // rely on buildings and/or map events to get new units.
        if (!this.map.getTile(at).isOccupied()) {
            this.map.getTile(at).insertUnit(player, player.getAllUnitTypes().get(name).clone());
        }
        return false;
    }
    public synchronized void endTurn () {
        if (this.map != null) {
            this.map.endTurn();
        }
    }
}
