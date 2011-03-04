package marten.aoe.engine;

import java.util.ArrayList;
import java.util.HashMap;
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
    private PlayerDTO[] playerList;
    private int currentPlayer = 0;
    private final java.util.Map<PlayerDTO, List<EngineListener>> listeners = new HashMap<PlayerDTO, List<EngineListener>>();

    public Engine (String mapName, PlayerDTO[] playerList) {
        this.map = MapLoader.loadMap(this, mapName);
        this.playerList = playerList;
    }

    public synchronized void switchMap (String mapName, PlayerDTO[] playerList) {
        this.map = MapLoader.loadMap(this, mapName);
        this.playerList = playerList;
        this.currentPlayer = 0;
        this.invokeGlobalEvent(GlobalEvent.MAP_CHANGE);
    }

    public synchronized PlayerDTO getActivePlayer() {
        return this.playerList[this.currentPlayer];
    }
    public synchronized PlayerDTO[] getAllPlayers() {
        return this.playerList;
    }
    public synchronized Map getMap() {
        return this.map;
    }
    public synchronized void addListener (EngineListener listener, PlayerDTO player) {
        if (listener == null || player == null) {
            throw new IllegalArgumentException("Null arguments are not accepted.");
        }
        if (!this.listeners.containsKey(player)) {
            this.listeners.put(player, new ArrayList<EngineListener>());
        }
        this.listeners.get(player).add(listener);
    }
    public synchronized void removeListener (EngineListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Null arguments are not accepted.");
        }
        this.listeners.remove(listener);
    }
    public synchronized MapDTO getMapDTO (PlayerDTO player) {
        return this.map.getDTO(player);
    }
    public synchronized FullMapDTO getFullMapDTO (PlayerDTO player) {
        return this.map.getFullDTO(player);
    }
    public synchronized TileDTO getTileDTO (PlayerDTO player, PointDTO location) {
        return this.map.getTile(location).getDTO(player);
    }
    public synchronized FullTileDTO getFullTileDTO (PlayerDTO player, PointDTO location) {
        return this.map.getTile(location).getFullDTO(player);
    }
    public synchronized UnitDTO getUnitDTO (PlayerDTO player, PointDTO location) {
        Unit unit = this.map.getTile(location).getUnit();
        return (unit != null ? unit.getDTO(player) : null);
    }
    public synchronized FullUnitDTO getFullUnitDTO (PlayerDTO player, PointDTO location) {
        Unit unit = this.map.getTile(location).getUnit();
        return (unit != null ? unit.getFullDTO(player) : null);
    }
    @Deprecated public synchronized boolean moveUnit (PlayerDTO player, PointDTO from, PointDTO to) {
        return this.map.moveUnit(player, from, to);
    }
    @Deprecated public synchronized boolean createUnit (PlayerDTO player, String name, PointDTO at) {
        UnitLoader.loadUnit(name, player, this.map.getTile(at));
        return true;
    }
    public synchronized void endTurn (PlayerDTO player) {
        this.currentPlayer++;
        if (this.currentPlayer == this.playerList.length) {
            this.currentPlayer = 0;
        }
        this.map.endTurn();
        this.invokeGlobalEvent(GlobalEvent.TURN_END);
    }
    public synchronized boolean performAction (PlayerDTO player, PointDTO from, int action, PointDTO to) {
        Unit activeUnit = this.map.getTile(from).getUnit();
        // QUITE UGLY CODE FOLLOWS
        switch (action) {
            case 1:
                activeUnit.specialAction1(to); break;
            case 2:
                activeUnit.specialAction2(to); break;
            case 3:
                activeUnit.specialAction3(to); break;
            case 4:
                activeUnit.specialAction4(to); break;
            case 5:
                activeUnit.specialAction5(to); break;
            case 6:
                activeUnit.specialAction6(to); break;
            case 7:
                activeUnit.specialAction7(to); break;
            case 8:
                activeUnit.specialAction8(to); break;
            case 9:
                activeUnit.specialAction9(to); break;
        }
        return true;
    }
    public void invokePlayerSpecificLocalEvent(LocalEvent event, Tile location, PlayerDTO player) {
        if (event == null || location == null || player == null) {
            throw new IllegalArgumentException("Null arguments are not accepted.");
        }
        for (EngineListener listener : this.listeners.get(player)) {
            listener.onLocalEvent(event, location.getDTO(player));
        }
    }
    public void invokeLocalEvent(LocalEvent event, Tile location) {
        if (event == null || location == null) {
            throw new IllegalArgumentException("Null arguments are not accepted.");
        }
        for (PlayerDTO player : this.listeners.keySet()) {
            if (location.isVisible(player)) {
                for (EngineListener listener : this.listeners.get(player)) {
                    listener.onLocalEvent(event, location.getDTO(player));
                }
            }
        }
    }
    public void invokeGlobalEvent(GlobalEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Null arguments are not accepted.");
        }
        for (List<EngineListener> listenerList : this.listeners.values()) {
            for (EngineListener listener : listenerList) {
                listener.onGlobalEvent(event);
            }
        }
    }
    public void invokeLocalEventConcealUnit(LocalEvent event, Tile location) {
        if (event == null || location == null) {
            throw new IllegalArgumentException("Null arguments are not accepted.");
        }
        for (PlayerDTO player : this.listeners.keySet()) {
            if (location.isVisible(player)) {
                for (EngineListener listener : this.listeners.get(player)) {
                    listener.onLocalEvent(event, location.getDTOConcealUnit(player));
                }
            }
        }
    }
}
