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

import org.junit.Assert;

/** The main interface to the AoE engine, which should be used by all connected clients to perform operations on the engine.
 * @author Petras Ražanskas*/
public final class Engine {
    private Map map;
    private final PlayerDTO[] playerList;
    private int currentPlayer = 0;
    private final java.util.Map<PlayerDTO, List<EngineListener>> listeners = new HashMap<PlayerDTO, List<EngineListener>>();
    /** Initializes the engine with given map and players.
     * @param mapName - the name of the Map subclass to be loaded.
     * @param playerList - an array of Player instances that will participate in this battle. */
    public Engine (String mapName, PlayerDTO[] playerList) {
        this.map = MapLoader.loadMap(this, mapName);
        Assert.assertNotNull(this.map);
        this.playerList = playerList;
        Assert.assertNotNull(this.playerList);
        Assert.assertTrue(this.playerList.length != this.map.getPlayerLimit());
    }
    /** Switches the map of the engine to a new one (useful for multi-map scenarios).
     * @param mapName - the name of the new Map subclass to be loaded.*/
    public synchronized void switchMap (String mapName) {
        this.map = MapLoader.loadMap(this, mapName);
        Assert.assertNotNull(this.map);
        this.invokeGlobalEvent(GlobalEvent.MAP_CHANGE);
    }
    /** @return the description of the currently active player.*/
    public synchronized PlayerDTO getActivePlayer() {
        return this.playerList[this.currentPlayer];
    }
    /** Registers a new listener for this engine to intercept engine events.
     * @param listener - the new engine listener.
     * @param player - the player this listener represents*/
    public synchronized void addListener (EngineListener listener, PlayerDTO player) {
        Assert.assertNotNull(listener);
        Assert.assertNotNull(player);
        if (!this.listeners.containsKey(player)) {
            this.listeners.put(player, new ArrayList<EngineListener>());
        }
        this.listeners.get(player).add(listener);
    }
    /** Unregisters a listener from this engine so it no longer receives engine events.
     * @param listener - the listener to be unregistered.*/
    public synchronized void removeListener (EngineListener listener) {
        Assert.assertNotNull(listener);
        this.listeners.remove(listener);
    }
    /** @return the full short description of the map as applicable to the given player.
     * @param player - the player to whom this information concerns.*/
    public synchronized MapDTO getMapDTO (PlayerDTO player) {
        Assert.assertNotNull(player);
        return this.map.getDTO(player);
    }
    /** @return the full verbose description of the map as applicable to the given player.
     * @param player - the player to whom this information concerns.*/
    public synchronized FullMapDTO getFullMapDTO (PlayerDTO player) {
        Assert.assertNotNull(player);
        return this.map.getFullDTO(player);
    }
    /** @return the full short description of a tile at the given location.
     * @param player - the player to whom this information concerns.
     * @param location - the location of the concerned tile.*/
    public synchronized TileDTO getTileDTO (PlayerDTO player, PointDTO location) {
        Assert.assertNotNull(player);
        Assert.assertNotNull(location);
        Tile tile = this.map.getTile(location);
        Assert.assertNotNull(tile);
        return tile.getDTO(player);
    }
    /** @return the full verbose description of a tile at the given location.
     * @param player - the player to whom this information concerns.
     * @param location - the location of the concerned tile.*/
    public synchronized FullTileDTO getFullTileDTO (PlayerDTO player, PointDTO location) {
        Assert.assertNotNull(player);
        Assert.assertNotNull(location);
        Tile tile = this.map.getTile(location);
        Assert.assertNotNull(tile);
        return tile.getFullDTO(player);
    }
    /** @return the full short description of a unit at the given location <b>or</b> <code>null</code> if there is no unit there.
     * @param player - the player to whom this information concerns.
     * @param location - the location of the concerned unit.*/
    public synchronized UnitDTO getUnitDTO (PlayerDTO player, PointDTO location) {
        Assert.assertNotNull(player);
        Assert.assertNotNull(location);
        Tile tile = this.map.getTile(location);
        Assert.assertNotNull(tile);
        Unit unit = tile.getUnit();
        return (unit != null ? unit.getDTO(player) : null);
    }
    /** @return the full verbose description of a unit at the given location <b>or</b> <code>null</code> if there is no unit there.
     * @param player - the player to whom this information concerns.
     * @param location - the location of the concerned unit.*/
    public synchronized FullUnitDTO getFullUnitDTO (PlayerDTO player, PointDTO location) {
        Assert.assertNotNull(player);
        Assert.assertNotNull(location);
        Tile tile = this.map.getTile(location);
        Assert.assertNotNull(tile);
        Unit unit = tile.getUnit();
        return (unit != null ? unit.getFullDTO(player) : null);
    }
    /** Moves a unit from one location to another, provided such unit exists.
     * @return <code>true</code> if the movement was successful, <code>false</code> otherwise (situation on the map remains unchanged).
     * @param player - the player who is performing the movement.
     * @param from - the location from where the movement commences.
     * @param to - the location to which the movement commences.
     * @deprecated The players should rely on unit actions to perform movement.*/
    @Deprecated public synchronized boolean moveUnit (PlayerDTO player, PointDTO from, PointDTO to) {
        Assert.assertNotNull(player);
        Assert.assertNotNull(from);
        Assert.assertNotNull(to);
        return this.map.moveUnit(player, from, to);
    }
    /** Creates a unit at the given location, provided the location is empty.
     * @return <code>true</code> if unit creation was successful, <code>false</code> otherwise (situation on the map remains unchanged).
     * @param player - the player who is creatinf the unit.
     * @param name - the name of the Unit subclass to be instantiated.
     * @param at - the location where the Unit is to be created.
     * @deprecated The players should rely on their buildings or map events to provide new units.*/
    @Deprecated public synchronized boolean createUnit (PlayerDTO player, String name, PointDTO at) {
        Assert.assertNotNull(player);
        Assert.assertNotNull(name);
        Assert.assertNotNull(at);
        Tile activeTile = this.map.getTile(at);
        Assert.assertNotNull(activeTile);
        if (!activeTile.isOccupied()) {
            UnitLoader.loadUnit(name, player, activeTile);
            return true;
        }
        return false;
    }
    /** Terminates the turn, if its the given player's turn.
     * @return <code>false</code> if it is not the given player's turn (nothing changes), <code>true</code> otherwise.
     * @param player - the player who attempts to end his turn. */
    public synchronized boolean endTurn (PlayerDTO player) {
        Assert.assertNotNull(player);
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
     * @param target - the location which is supposed to be affected by the action.*/
    public synchronized boolean performAction (PlayerDTO player, PointDTO actor, int action, PointDTO target) {
        Assert.assertNotNull(player);
        Assert.assertNotNull(actor);
        Assert.assertFalse(action < 1);
        Assert.assertFalse(action > 9);
        Assert.assertNotNull(target);
        if (player != this.playerList[this.currentPlayer]) {
            return false;
        }
        Tile activeTile = this.map.getTile(actor);
        Assert.assertNotNull(activeTile);
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
    /** Forwards all local events to the listeners of this engine.
     * @param event - the type of event.
     * @param location - the tile where the event happened.*/
    public void invokeLocalEvent(LocalEvent event, Tile location) {
        Assert.assertNotNull(event);
        Assert.assertNotNull(location);
        for (PlayerDTO player : this.listeners.keySet()) {
            if (location.isVisible(player)) {
                for (EngineListener listener : this.listeners.get(player)) {
                    listener.onLocalEvent(event, location.getDTO(player));
                }
            }
        }
    }
    /** Forwards all global events to the listeners of this engine.
     * @param event - the type of event.*/
    public void invokeGlobalEvent(GlobalEvent event) {
        Assert.assertNotNull(event);
        for (List<EngineListener> listenerList : this.listeners.values()) {
            for (EngineListener listener : listenerList) {
                listener.onGlobalEvent(event);
            }
        }
    }
}
