package marten.aoe.engine;

import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.engine.loader.MapLoader;
import marten.aoe.engine.loader.UnitLoader;

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
        call(boolean Engine.performAction(PlayerDTO, PointDTO, int, PointDTO)) &&
        target(engine) &&
        args(player, from, action, to);
    
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
    /*
    after() returning(Engine engine) : initiateEngine(*, *) {
        this.validateNewMap(engine);
    }
    /*
    after(Engine engine) : switchMap(engine, *, *) {
        this.validateNewMap(engine);
    }
    */
    before(Engine engine, PlayerDTO player) : retrieveGlobalData(engine, player) || retrieveLocalData(engine, player, *) {
        this.validatePlayer(engine, player);
    }
    /*
    before(Engine engine, PointDTO location) : retrieveLocalData(engine, *, location) {
        this.validateLocation(engine, location);
    }
    */
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
    before(Engine engine, PlayerDTO player) : endTurn(engine, player) {
        this.validatePlayer(engine, player);
    }
    void around(Engine engine, PlayerDTO player) : endTurn(engine, player) {
        if (engine.getActivePlayer() == player) {
            proceed(engine, player);
        }
    }
    /*
    before(Engine engine, PlayerDTO player, PointDTO from, int action, PointDTO to) : performAction(engine, player, from, action, to) {
        this.validatePlayer(engine, player);
        this.validateLocation(engine, from);
        this.validateLocation(engine, to);
        if (action < 1 || action > 9) {
            throw new IllegalArgumentException("Action index must be from 1 to 9 inclusive.");
        }
    }
    */
    /*
    boolean around(Engine engine, PlayerDTO player, PointDTO from, int action, PointDTO to) : performAction(engine, player, from, action, to) {
        if (engine.getActivePlayer() == player && engine.getMap().getTile(from).isOccupied() && engine.getMap().getTile(from).getUnit().getOwner() == player) {
            return proceed(engine, player, from, action, to);
        }
        return false;
    }
    */
    /*
    private void validateNewMap(Engine engine) {
        if (engine.getMap().getPlayerLimit() < engine.getAllPlayers().length) {
            throw new IllegalArgumentException("There are more players than slots provided by the map.");
        }
        for (int x = 0; x <= engine.getMap().getWidth(); x++) {
            for (int y = 0; y <= engine.getMap().getHeight(); y++) {
                if (engine.getMap().getTile(new PointDTO(x, y)) == null) {
                    throw new IllegalArgumentException("The loaded map is corrupted: some of the tiles are null.");
                }
            }
        }
    }*/
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
