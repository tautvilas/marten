package marten.aoe.engine;

import org.aspectj.lang.annotation.SuppressAjWarnings;

import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.engine.loader.MapLoader;
import marten.aoe.engine.loader.UnitLoader;

/** This aspect is used to validate every request that targets AoE engine for legality of its arguments.
 * Most of the times violations end in the {@link IllegalArgumentException}, although in several cases the validator fails silently and the request is dropped.
 * @author Petras Ražanskas*/
@SuppressAjWarnings({"adviceDidNotMatch"}) 
public final aspect EngineRequestValidator {
    pointcut initiateEngine(String mapName, PlayerDTO[] playerList) : 
        call(Engine.new(String, PlayerDTO[])) &&
        args(mapName, playerList);
    pointcut switchMap(Engine engine, String mapName, PlayerDTO[] playerList) : 
        call(void Engine.switchMap(String, PlayerDTO[])) &&
        target(engine) &&
        args(mapName, playerList);
    pointcut retrieveGlobalData(Engine engine, PlayerDTO player) :
        call(* Engine.get*(PlayerDTO)) && 
        target(engine) && 
        args(player);
    pointcut retrieveLocalData(Engine engine, PlayerDTO player, PointDTO location) :
        call(* Engine.get*(PlayerDTO, PointDTO)) && 
        target(engine) && 
        args(player, location);
    /* FIXME: The following pointcut targets a deprecated method */
    pointcut moveUnit(Engine engine, PlayerDTO player, PointDTO from, PointDTO to) :
        call(boolean Engine.moveUnit(PlayerDTO, PointDTO, PointDTO)) && 
        target(engine) && 
        args(player, from, to);
    /* FIXME: The following pointcut targets a deprecated method */
    pointcut createUnit(Engine engine, PlayerDTO player, String unitName, PointDTO location) :
        call(boolean Engine.createUnit(PlayerDTO, String, PointDTO)) && 
        target(engine) && 
        args(player, unitName, location);
    pointcut endTurn(Engine engine, PlayerDTO player) :
        call(void Engine.endTurn(PlayerDTO)) &&
        target(engine) &&
        args(player);
    pointcut performAction(Engine engine, PlayerDTO player, PointDTO from, int action, PointDTO to) :
        call(void Engine.performAction(PlayerDTO, PointDTO, int, PointDTO)) &&
        target(engine) &&
        args(player, from, action, to);
 
    /** Whenever the map and/or player list is updated in the engine, make sure that:
     * 1) neither the provided map name nor the provided player list are null;
     * 2) the player list does not contain any {@code null} references;
     * 3) the map with the given name does actually exist.
     * @param mapName - the name of the map that is attempted to be loaded.
     * @param playerList - the new list of players to be used in the engine.*/
    before(String mapName, PlayerDTO[] playerList) : initiateEngine(mapName, playerList) || switchMap(*, mapName, playerList){
        if (mapName == null) {
            throw new IllegalArgumentException("Supplied map name is null.");
        }
        if (!MapLoader.getAvailableMaps().contains(mapName)) {
            throw new IllegalArgumentException("Supplied map name does not correspond to any map.");
        }
        if (playerList == null) {
            throw new IllegalArgumentException("Supplied player list is null.");            
        }
        for (PlayerDTO player : playerList) {
            if (player == null) {
                throw new IllegalArgumentException("Supplied player list contains one or more null entries.");
            }
        }
    }
    
    /** Whenever the map is finished loading during engine initialization, make sure that:
     * 1) the map provides as many or more slots as there are players involved;
     * 2) that there are no {@code null} tiles in the map.
     * @param engine - the engine that has completed the loading procedure.*/
    after() returning(Engine engine) : initiateEngine(*, *) {
        this.validateNewMap(engine);
    }
    
    /** Whenever the map is finished loading after the map switch, make sure that:
     * 1) the map provides as many or more slots as there are players involved;
     * 2) that there are no {@code null} tiles in the map.
     * @param engine - the engine that has completed the loading procedure.*/
    after(Engine engine) : switchMap(engine, *, *) {
        this.validateNewMap(engine);
    }
    
    /** Whenever a player attempts to obtain global data from the engine (pertaining to the whole map),
     * or local data (pertaining to a single cell of the map), make sure that the player is authorized on this engine.
     * @param engine - the engine where the data retrieval is attempted.
     * @param player - the player who is attempting to retrieve data.*/
    before(Engine engine, PlayerDTO player) : retrieveGlobalData(engine, player) || retrieveLocalData(engine, player, *) {
        this.validatePlayer(engine, player);
    }
    
    /** Whenever a player attempts to obtain local data from the engine (pertaining to a single cell of the map),
     * make sure that the location of data retrieval is inside the boundaries of the map.
     * @param engine - the engine where the data retrieval is attempted.
     * @param location - the location of the cell, where the data should be retrieved.*/
    before(Engine engine, PointDTO location) : retrieveLocalData(engine, *, location) {
        this.validateLocation(engine, location);
    }
    
    /* FIXME: The following advice targets a deprecated method */
    before(Engine engine, PlayerDTO player, PointDTO from, PointDTO to) : moveUnit(engine, player, from, to) {
        this.validatePlayer(engine, player);
        this.validateLocation(engine, to);
        this.validateLocation(engine, from);
    }
    
    /* FIXME: The following advice targets a deprecated method */
    boolean around(Engine engine, PlayerDTO player, PointDTO from, PointDTO to) : moveUnit(engine, player, from, to) {
        if (engine.getActivePlayer() == player) {
            return proceed(engine, player, from, to);
        }
        return false;
    }
    
    /* FIXME: The following advice targets a deprecated method */
    before(Engine engine, PlayerDTO player, String unitName, PointDTO location) : createUnit(engine, player, unitName, location) {
        this.validatePlayer(engine, player);
        if (!UnitLoader.getAvailableUnits().contains(unitName)) {
            throw new IllegalArgumentException("Supplied unit name does not correspond to any unit.");
        }
        this.validateLocation(engine, location);
    }
    
    /* FIXME: The following advice targets a deprecated method */
    boolean around(Engine engine, PlayerDTO player, String unitName, PointDTO location) : createUnit(engine, player, unitName, location) {
        if (engine.getActivePlayer() == player && !engine.getMap().getTile(location).isOccupied()) {
            return proceed(engine, player, unitName, location);
        }
        return false;
    }
    
    /** Whenever a player attempts to end his turn controlling the engine, make sure that:
     * 1) the player actually has access to this engine;
     * 2) the player is the current active player.
     * @param engine - the engine where the end-turn procedure is attempted.
     * @param player - the player who attemps to end his turn.*/
    void around(Engine engine, PlayerDTO player) : endTurn(engine, player) {
        this.validatePlayer(engine, player);
        if (engine.getActivePlayer() == player) {
            proceed(engine, player);
        }
    }
    
    /** Whenever a player attempts an action on the engine, make sure that:
     * 1) the player actually has access to this engine;
     * 2) neither the acting tile nor the target tile are outside of the map boundaries;
     * 3) the action index is with the legal range (from 1 to 9 inclusive);
     * 4) the player is the current active player;
     * 5) the acting tile does actually include a unit that belongs to the player.
     * @param engine - the engine where the action is attempted;
     * @param player - the player who is attempting the action;
     * @param from - the location of the acting tile;
     * @param action - the index of the action attempted;
     * @param to - the target of the attempted action.*/
    void around(Engine engine, PlayerDTO player, PointDTO from, int action, PointDTO to) : performAction(engine, player, from, action, to) {
        this.validatePlayer(engine, player);
        this.validateLocation(engine, from);
        this.validateLocation(engine, to);
        if (action < 1 || action > 9) {
            throw new IllegalArgumentException("Action index must be from 1 to 9 inclusive.");
        }
        if (engine.getActivePlayer() == player && engine.getMap().getTile(from).isOccupied() && engine.getMap().getTile(from).getUnit().getOwner() == player) {
            proceed(engine, player, from, action, to);
        }
    }
    
    private void validateNewMap(Engine engine) {
        if (engine.getMap().getPlayerLimit() < engine.getAllPlayers().length) {
            throw new IllegalArgumentException("There are more players than slots provided by the map.");
        }
        for (int x = 0; x < engine.getMap().getWidth(); x++) {
            for (int y = 0; y < engine.getMap().getHeight(); y++) {
                if (engine.getMap().getTile(new PointDTO(x, y)) == null) {
                    throw new IllegalArgumentException("The loaded map is corrupted: some of the tiles are null.");
                }
            }
        }
    }
    private void validateLocation(Engine engine, PointDTO location) {
        if (location.getX() < 0 || location.getX() >= engine.getMap().getWidth() || location.getY() < 0 || location.getY() >= engine.getMap().getHeight()) {
            throw new IllegalArgumentException("The requested location is out of map bounds.");
        }
    }
    private void validatePlayer(Engine engine, PlayerDTO player) {
        if (player == null) {
            throw new IllegalArgumentException("A null player cannot operate on the engine.");
        }
        boolean isPlayerValid = false;
        for (PlayerDTO validPlayer : engine.getAllPlayers()) {
            isPlayerValid |= (validPlayer == player);
        }
        if (!isPlayerValid) {
            throw new IllegalArgumentException("This player is not registered on this engine.");
        }
    }
}
