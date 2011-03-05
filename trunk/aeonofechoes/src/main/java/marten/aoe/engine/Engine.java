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

/** The main access point to all functionality of the AoE engine.
 * Every call to methods of this class is supervised by {@link EngineRequestValidator}.
 * Monitoring of internal changes in the engine is provided by {@link EngineMonitor}.
 * @author Petras Ražanskas*/
public final class Engine {
    private Map map;
    private PlayerDTO[] playerList;
    private int currentPlayer = 0;
    private final java.util.Map<PlayerDTO, List<EngineListener>> listeners = new HashMap<PlayerDTO, List<EngineListener>>();

    /** Constructs a new Engine initialized with provided map and list of players.
     * The map is loaded as a part of this constructor.
     * @param mapname - the name of the class (subclass of {@link Map}) to be loaded.
     * @param playerList - an array of players that will be allowed access to this engine.*/
    public Engine (String mapName, PlayerDTO[] playerList) {
        this.map = MapLoader.loadMap(this, mapName);
        this.playerList = playerList;
    }

    /** Switches the engine to another map with provided name, and with a new list of players.
     * The old map is unloaded and the new one is loaded as part of this procedure.
     * @param mapname - the name of the class (subclass of {@link Map}) to be loaded.
     * @param playerList - a new array of players that will be allowed access to this engine.*/
    public synchronized void switchMap (String mapName, PlayerDTO[] playerList) {
        this.map = MapLoader.loadMap(this, mapName);
        this.playerList = playerList;
        this.currentPlayer = 0;
        this.invokeGlobalEvent(GlobalEvent.MAP_CHANGE);
    }

    /** @return the player data for the currently active player.*/
    public synchronized PlayerDTO getActivePlayer() {
        return this.playerList[this.currentPlayer];
    }

    /** @return the complete list of players who are allowed access to this engine.*/
    public synchronized PlayerDTO[] getAllPlayers() {
        return this.playerList;
    }

    /** @return the map that is currently loaded in the engine.*/
    public synchronized Map getMap() {
        return this.map;
    }

    // FIXME: move this to EngineMonitor
    public synchronized void addListener (EngineListener listener, PlayerDTO player) {
        if (listener == null || player == null) {
            throw new IllegalArgumentException("Null arguments are not accepted.");
        }
        if (!this.listeners.containsKey(player)) {
            this.listeners.put(player, new ArrayList<EngineListener>());
        }
        this.listeners.get(player).add(listener);
    }

    // FIXME: move this to EngineMonitor
    public synchronized void removeListener (EngineListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Null arguments are not accepted.");
        }
        this.listeners.remove(listener);
    }

    /** @return the brief description of the map as seen from the perspective of the given player.
     * @param player - the player who is requesting the data.*/
    public synchronized MapDTO getMapDTO (PlayerDTO player) {
        return this.map.getDTO(player);
    }

    /** @return the verbose description of the map as seen from the perspective of the given player.
     * @param player - the player who is requesting the data.*/
    public synchronized FullMapDTO getFullMapDTO (PlayerDTO player) {
        return this.map.getFullDTO(player);
    }

    /** @return the brief description of the tile at given coordinates as seen from the perspective of the given player.
     * @param player - the player who is requesting the data.
     * @param location - the coordinates at which the queried tile is located.*/
    public synchronized TileDTO getTileDTO (PlayerDTO player, PointDTO location) {
        return this.map.getTile(location).getDTO(player);
    }

    /** @return the verbose description of the tile at given coordinates as seen from the perspective of the given player.
     * @param player - the player who is requesting the data.
     * @param location - the coordinates at which the queried tile is located.*/
    public synchronized FullTileDTO getFullTileDTO (PlayerDTO player, PointDTO location) {
        return this.map.getTile(location).getFullDTO(player);
    }

    /** @return the brief description of the unit at given coordinates as seen from the perspective of the given player, or {@code null} if no unit exists there.
     * @param player - the player who is requesting the data.
     * @param location - the coordinates at which the queried unit is located.*/
    public synchronized UnitDTO getUnitDTO (PlayerDTO player, PointDTO location) {
        Unit unit = this.map.getTile(location).getUnit();
        return (unit != null ? unit.getDTO(player) : null);
    }

    /** @return the verbose description of the unit at given coordinates as seen from the perspective of the given player, or {@code null} if no unit exists there.
     * @param player - the player who is requesting the data.
     * @param location - the coordinates at which the queried unit is located.*/
    public synchronized FullUnitDTO getFullUnitDTO (PlayerDTO player, PointDTO location) {
        Unit unit = this.map.getTile(location).getUnit();
        return (unit != null ? unit.getFullDTO(player) : null);
    }

    // FIXME: remove this at the first possible opportunity
    @Deprecated public synchronized boolean moveUnit (PlayerDTO player, PointDTO from, PointDTO to) {
        return this.map.moveUnit(player, from, to);
    }

    // FIXME: remove this at the first possible opportunity
    @Deprecated public synchronized boolean createUnit (PlayerDTO player, String name, PointDTO at) {
        UnitLoader.loadUnit(name, player, this.map.getTile(at));
        return true;
    }

    /** Cause the end of turn sequence on the map and relinquish control to another player.
     * @param player - the player who is finishing his turn.*/
    public synchronized void endTurn (PlayerDTO player) {
        this.currentPlayer++;
        if (this.currentPlayer == this.playerList.length) {
            this.currentPlayer = 0;
        }
        this.map.endTurn();
        this.invokeGlobalEvent(GlobalEvent.TURN_END);
    }

    /** Cause a unit in the "from" location to perform the given action on the "to" location.
     * @param player - the player who is attempting to perform an action.
     * @param from - the location from which the action originates.
     * @param action - the index of the action which is to be performed.
     * @param to - the location which is meant to be influenced by this action.*/
    public synchronized void performAction (PlayerDTO player, PointDTO from, int action, PointDTO to) {
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
    }

    // FIXME: move this functionality to EngineMonitor
    public void invokePlayerSpecificLocalEvent(LocalEvent event, Tile location, PlayerDTO player) {
        if (event == null || location == null || player == null) {
            throw new IllegalArgumentException("Null arguments are not accepted.");
        }
        for (EngineListener listener : this.listeners.get(player)) {
            listener.onLocalEvent(event, location.getDTO(player));
        }
    }

    // FIXME: move this functionality to EngineMonitor
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

    // FIXME: move this functionality to EngineMonitor
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

    // FIXME: move this functionality to EngineMonitor
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
