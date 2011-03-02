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

/** The main node of the AoE engine, which should be used by all connected clients to perform operations on the engine.
 * @author Petras Ražanskas*/
public final class Engine {
    private Map map;
    private final PlayerDTO[] playerList;
    private int currentPlayer = 0;
    private final java.util.Map<PlayerDTO, List<EngineListener>> listeners = new HashMap<PlayerDTO, List<EngineListener>>();
    /** Initializes the engine with given map and players.
     * @param mapName - the name of the Map subclass to be loaded.
     * @param playerList - an array of Player instances that will participate in this battle.
     * @throws IllegalArgumentException if any of the parameters are <code>null</code> <b>or</b> the list of players contains any <code>null</code> entries <b>or</b> the list of players includes more or less players than defined for the map.*/
    public Engine (String mapName, PlayerDTO[] playerList) {
        if (mapName == null || playerList == null) {
            throw new IllegalArgumentException("Null arguments are not accepted");
        }
        for (PlayerDTO player : playerList) {
            if (player == null) {
                throw new IllegalArgumentException("Player list may not contain null players");
            }
        }
        this.map = MapLoader.loadMap(this, mapName);
        this.playerList = playerList;
        if (this.playerList.length > this.map.getPlayerLimit()) {
            throw new IllegalArgumentException("Player list must provide as many players as the map requires");
        }
    }
    /** Switches the map of the engine to a new one (useful for multi-map scenarios).
     * @param mapName - the name of the new Map subclass to be loaded.
     * @throws IllegalArgumentException if the argument is <code>null</code>*/
    public synchronized void switchMap (String mapName) {
        if (mapName == null) {
            throw new IllegalArgumentException("Null arguments are not accepted.");
        }
        this.map = MapLoader.loadMap(this, mapName);
        this.invokeGlobalEvent(GlobalEvent.MAP_CHANGE);
    }
    /** @return the description of the currently active player.*/
    public synchronized PlayerDTO getActivePlayer() {
        return this.playerList[this.currentPlayer];
    }
    /** Registers a new listener for this engine to intercept engine events.
     * @param listener - the new engine listener.
     * @param player - the player this listener represents.
     * @throws IllegalArgumentException if any of the arguments is <code>null</code>.*/
    public synchronized void addListener (EngineListener listener, PlayerDTO player) {
        if (listener == null || player == null) {
            throw new IllegalArgumentException("Null arguments are not accepted.");
        }
        if (!this.listeners.containsKey(player)) {
            this.listeners.put(player, new ArrayList<EngineListener>());
        }
        this.listeners.get(player).add(listener);
    }
    /** Unregisters a listener from this engine so it no longer receives engine events.
     * @param listener - the listener to be unregistered.
     * @throws IllegalArgumentException if the argument is <code>null</code>.*/
    public synchronized void removeListener (EngineListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Null arguments are not accepted.");
        }
        this.listeners.remove(listener);
    }
    /** @return the full short description of the map as applicable to the given player.
     * @param player - the player to whom this information concerns.
     * @throws IllegalArgumentException if the argument is <code>null</code>.*/
    public synchronized MapDTO getMapDTO (PlayerDTO player) {
        if (player == null) {
            throw new IllegalArgumentException("Null arguments are not accepted.");
        }
        return this.map.getDTO(player);
    }
    /** @return the full verbose description of the map as applicable to the given player.
     * @param player - the player to whom this information concerns.
     * @throws IllegalArgumentException if the argument is <code>null</code>.*/
    public synchronized FullMapDTO getFullMapDTO (PlayerDTO player) {
        if (player == null) {
            throw new IllegalArgumentException("Null arguments are not accepted.");
        }
        return this.map.getFullDTO(player);
    }
    /** @return the full short description of a tile at the given location.
     * @param player - the player to whom this information concerns.
     * @param location - the location of the concerned tile.
     * @throws IllegalArgumentException if any of the arguments are <code>null</code>.*/
    public synchronized TileDTO getTileDTO (PlayerDTO player, PointDTO location) {
        if (player == null || location == null) {
            throw new IllegalArgumentException("Null arguments are not accepted.");
        }
        Tile tile = this.map.getTile(location);
        return tile.getDTO(player);
    }
    /** @return the full verbose description of a tile at the given location.
     * @param player - the player to whom this information concerns.
     * @param location - the location of the concerned tile.
     * @throws IllegalArgumentException if any of the arguments are <code>null</code>.*/
    public synchronized FullTileDTO getFullTileDTO (PlayerDTO player, PointDTO location) {
        if (player == null || location == null) {
            throw new IllegalArgumentException("Null arguments are not accepted.");
        }
        Tile tile = this.map.getTile(location);
        return tile.getFullDTO(player);
    }
    /** @return the full short description of a unit at the given location <b>or</b> <code>null</code> if there is no unit there.
     * @param player - the player to whom this information concerns.
     * @param location - the location of the concerned unit.
     * @throws IllegalArgumentException if any of the arguments are <code>null</code>.*/
    public synchronized UnitDTO getUnitDTO (PlayerDTO player, PointDTO location) {
        if (player == null || location == null) {
            throw new IllegalArgumentException("Null arguments are not accepted.");
        }
        Tile tile = this.map.getTile(location);
        Unit unit = tile.getUnit();
        return (unit != null ? unit.getDTO(player) : null);
    }
    /** @return the full verbose description of a unit at the given location <b>or</b> <code>null</code> if there is no unit there.
     * @param player - the player to whom this information concerns.
     * @param location - the location of the concerned unit.
     * @throws IllegalArgumentException if any of the arguments are <code>null</code>.*/
    public synchronized FullUnitDTO getFullUnitDTO (PlayerDTO player, PointDTO location) {
        if (player == null || location == null) {
            throw new IllegalArgumentException("Null arguments are not accepted.");
        }
        Tile tile = this.map.getTile(location);
        Unit unit = tile.getUnit();
        return (unit != null ? unit.getFullDTO(player) : null);
    }
    /** Moves a unit from one location to another, provided such unit exists.
     * @return <code>true</code> if the movement was successful, <code>false</code> otherwise (situation on the map remains unchanged).
     * @param player - the player who is performing the movement.
     * @param from - the location from where the movement commences.
     * @param to - the location to which the movement commences.
     * @deprecated The players should rely on unit actions to perform movement.
     * @throws IllegalArgumentException if any of the arguments are <code>null</code>.*/
    @Deprecated public synchronized boolean moveUnit (PlayerDTO player, PointDTO from, PointDTO to) {
        if (player == null || from == null || to == null) {
            throw new IllegalArgumentException("Null arguments are not accepted.");
        }
        return this.map.moveUnit(player, from, to);
    }
    /** Creates a unit at the given location, provided the location is empty.
     * @return <code>true</code> if unit creation was successful, <code>false</code> otherwise (situation on the map remains unchanged).
     * @param player - the player who is creatinf the unit.
     * @param name - the name of the Unit subclass to be instantiated.
     * @param at - the location where the Unit is to be created.
     * @deprecated The players should rely on their buildings or map events to provide new units.
     * @throws IllegalArgumentException if any of the arguments are <code>null</code>.*/
    @Deprecated public synchronized boolean createUnit (PlayerDTO player, String name, PointDTO at) {
        if (player == null || name == null || at == null) {
            throw new IllegalArgumentException("Null arguments are not accepted.");
        }
        Tile activeTile = this.map.getTile(at);
        if (!activeTile.isOccupied()) {
            UnitLoader.loadUnit(name, player, activeTile);
            return true;
        }
        return false;
    }
    /** Terminates the turn, if its the given player's turn.
     * @return <code>false</code> if it is not the given player's turn (nothing changes), <code>true</code> otherwise.
     * @param player - the player who attempts to end his turn.
     * @throws IllegalArgumentException if the argument is <code>null</code>.*/
    public synchronized boolean endTurn (PlayerDTO player) {
        if (player == null) {
            throw new IllegalArgumentException("Null arguments are not accepted.");
        }
        if (player != this.playerList[this.currentPlayer]) {
            return false;
        }
        this.currentPlayer++;
        if (this.currentPlayer == this.playerList.length) {
            this.currentPlayer = 0;
        }
        this.map.endTurn();
        this.invokeGlobalEvent(GlobalEvent.TURN_END);
        return true;
    }
    /** Performs one of nine special actions with a given unit on a given target.
     * @return <code>false</code> if either this is not the acting players turn <b>or</b> there is no unit in the active location, <code>true</code> otherwise.
     * @param player - the player trying to perform the action
     * @param actor - the location of the unit to perform the action.
     * @param action - an integer index (from 1 to 9) of the action to be performed.
     * @param target - the location which is supposed to be affected by the action.
     * @throws IllegalArgumentException if any of the arguments are <code>null</code> <b>or</b> <code>action</code> index is not from the interval from 1 to 9.*/
    public synchronized boolean performAction (PlayerDTO player, PointDTO actor, int action, PointDTO target) {
        if (player == null || actor == null || target == null) {
            throw new IllegalArgumentException("Null arguments are not accepted.");
        }
        if (action < 1 || action > 9) {
            throw new IllegalArgumentException("Action must be an index between 1 and 9.");
        }
        if (player != this.playerList[this.currentPlayer]) {
            return false;
        }
        Tile activeTile = this.map.getTile(actor);
        Unit activeUnit = activeTile.getUnit();
        if (activeUnit == null || player != activeUnit.getOwner()) {
            return false;
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
        }
        return true;
    }
    /** Forwards all local events to the listeners pertaining to given player.
     * @param event - the type of event.
     * @param location - the tile where the event happened.
     * @param player - the player to be notified about the event.
     * @throws IllegalArgumentException if any of the arguments are <code>null</code>.*/
    public void invokePlayerSpecificLocalEvent(LocalEvent event, Tile location, PlayerDTO player) {
        if (event == null || location == null || player == null) {
            throw new IllegalArgumentException("Null arguments are not accepted.");
        }
        for (EngineListener listener : this.listeners.get(player)) {
            listener.onLocalEvent(event, location.getDTO(player));
        }
    }
    /** Forwards all local events to the listeners of this engine.
     * @param event - the type of event.
     * @param location - the tile where the event happened.
     * @throws IllegalArgumentException if any of the arguments are <code>null</code>.*/
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
    /** Forwards all global events to the listeners of this engine.
     * @param event - the type of event.
     * @throws IllegalArgumentException if the argument is <code>null</code>.*/
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
