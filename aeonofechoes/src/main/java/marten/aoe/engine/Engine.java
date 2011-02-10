package marten.aoe.engine;

import java.util.ArrayList;
import java.util.List;

import marten.aoe.dto.FullMapDTO;
import marten.aoe.dto.FullTileDTO;
import marten.aoe.dto.FullUnitDTO;
import marten.aoe.dto.MapDTO;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.dto.UnitDTO;
import marten.aoe.engine.loader.MapLoader;
import marten.aoe.engine.loader.UnitLoader;

public final class Engine {
    private Map map;
    private final PlayerDTO[] playerList;
    private int currentPlayer = 0;
    private final List<EngineListener> listeners = new ArrayList<EngineListener>();
    public Engine (String mapName, PlayerDTO[] playerList) {
        this.map = MapLoader.loadMap(this, mapName);
        this.playerList = playerList;
    }
    public synchronized void switchMap (String mapName) {
        this.map = MapLoader.loadMap(this, mapName);
        this.invokeGlobalEvent(GlobalEvent.MAP_CHANGE);
    }
    public synchronized PlayerDTO getActivePlayer() {
        return this.playerList[this.currentPlayer];
    }
    public synchronized void addListener (EngineListener listener) {
        this.listeners.add(listener);
    }
    public synchronized void removeListener (EngineListener listener) {
        this.listeners.remove(listener);
    }
    public synchronized MapDTO getMinimalMapDTO (PlayerDTO player) {
        return (this.map != null) ? this.map.getMinimalDTO(player) : null;
    }
    public synchronized FullMapDTO getMapDTO (PlayerDTO player) {
        return (this.map != null) ? this.map.getDTO(player) : null;
    }
    public synchronized TileDTO getMinimalTileDTO (PlayerDTO player, PointDTO location) {
        return (this.map != null) ? this.map.getTile(location).getMinimalDTO(player) : null;
    }
    public synchronized FullTileDTO getTileDTO (PlayerDTO player, PointDTO location) {
        return (this.map != null) ? this.map.getTile(location).getDTO(player) : null;
    }
    public synchronized UnitDTO getMinimalUnitDTO (PlayerDTO player, PointDTO location) {
        return (this.map != null) ? this.map.getTile(location).getUnit().getMinimalDTO(player) : null;
    }
    public synchronized FullUnitDTO getUnitDTO (PlayerDTO player, PointDTO location) {
        return (this.map != null) ? this.map.getTile(location).getUnit().getDTO(player) : null;
    }
    @Deprecated public synchronized boolean moveUnit (PlayerDTO player, PointDTO from, PointDTO to) {
        // For testing purposes only. In normal circumstances the players should
        // rely on actions, defined in units, for movement.
        return this.map.moveUnit(player, from, to);
    }
    @Deprecated public synchronized boolean createUnit (PlayerDTO player, String name, PointDTO at) {
        // For testing purposes only. In normal circumstances the players should
        // rely on buildings and/or map events to get new units.
        Tile activeTile = this.map.getTile(at);
        if (!activeTile.isOccupied()) {
            UnitLoader.loadUnit(name, player, activeTile);
        }
        return false;
    }
    public synchronized void endTurn (PlayerDTO player) {
        if (player != this.playerList[this.currentPlayer] || this.map == null) {
            return;
        }
        this.currentPlayer++;
        if (this.currentPlayer == this.playerList.length) {
            this.currentPlayer = 0;
        }
        this.map.endTurn();
        this.invokeGlobalEvent(GlobalEvent.TURN_END);
    }
    public synchronized void performAction (PlayerDTO player, PointDTO actor, int action, PointDTO target) {
        if (player != this.playerList[this.currentPlayer] || this.map == null) {
            return;
        }
        Tile activeTile = this.map.getTile(actor);
        if (activeTile == null) {
            return;
        }
        Unit activeUnit = activeTile.getUnit();
        if (activeUnit == null || player != activeUnit.getOwner()) {
            return;
        }
        // QUITE UGLY CODE FOLLOWS
        switch (action) {
            case 1:
                activeUnit.specialAction1(target); break;
            case 2:
                activeUnit.specialAction2(target); break;
            case 3:
                activeUnit.specialAction3(target); break;
            case 4:
                activeUnit.specialAction4(target); break;
            case 5:
                activeUnit.specialAction5(target); break;
            case 6:
                activeUnit.specialAction6(target); break;
            case 7:
                activeUnit.specialAction7(target); break;
            case 8:
                activeUnit.specialAction8(target); break;
            case 9:
                activeUnit.specialAction9(target); break;
            default:
                throw new IllegalStateException("Illegal action index.");
        }
    }
    public void invokeLocalEvent(LocalEvent event, Tile location) {
        for (EngineListener listener : this.listeners) {
            listener.onLocalEvent(event, location.getDTO(listener.getAssignedPlayer()));
        }
    }
    public void invokeGlobalEvent(GlobalEvent event) {
        for (EngineListener listener : this.listeners) {
            listener.onGlobalEvent(event);
        }
    }
}
