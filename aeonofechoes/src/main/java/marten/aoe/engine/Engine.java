package marten.aoe.engine;

import marten.aoe.aspectj.NoNullEntries;
import marten.aoe.aspectj.NotNull;
import marten.aoe.dto.MapDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.dto.UnitDTO;
import marten.aoe.engine.aspectj.EngineMonitor;
import marten.aoe.engine.core.Map;
import marten.aoe.engine.core.PlayerDTO;
import marten.aoe.engine.core.Unit;
import marten.aoe.engine.loader.MapLoader;
import marten.aoe.engine.rules.MinimalRuleset;
import marten.aoe.engine.rules.Rules;

/** The main access point to all functionality of the AoE engine.
 * Monitoring of internal changes in the engine is provided by {@link EngineMonitor}.
 * @author Petras Ražanskas*/
public final class Engine {
    private Map map;
    private Rules ruleset;
    private PlayerDTO[] playerList;
    private int currentPlayer = 0;
    private int turnCounter = 1;

    /** Constructs a new Engine initialized with provided map and list of players.
     * The map is loaded as a part of this constructor.
     * @param mapname - the name of the class (subclass of {@link Map}) to be loaded.
     * @param playerList - an array of players that will be allowed access to this engine.*/
    public Engine (@NotNull String mapName, @NoNullEntries PlayerDTO[] playerList) {
        if (MapLoader.getAvailableMaps().contains(mapName)) {
            this.map = MapLoader.loadMap(this, mapName);            
        } else if (MapLoader.getAvailableSimpleMaps().contains(mapName)) {
            this.map = MapLoader.loadSimpleMap(this, mapName);
        } else {
            throw new IllegalArgumentException("Unknown map name");
        }
        this.playerList = playerList;
        this.validateNewMap();
        this.ruleset = new MinimalRuleset();
    }

    public void start() {
        this.ruleset.gameStart(this.map, playerList);
    }

    /** Switches the engine to another map with provided name, and with a new list of players.
     * The old map is unloaded and the new one is loaded as part of this procedure.
     * @param mapname - the name of the class (subclass of {@link Map}) to be loaded.
     * @param playerList - a new array of players that will be allowed access to this engine.*/
    public synchronized void switchMap (@NotNull String mapName, @NoNullEntries PlayerDTO[] playerList) {
        if (MapLoader.getAvailableMaps().contains(mapName)) {
            this.map = MapLoader.loadMap(this, mapName);            
        } else if (MapLoader.getAvailableSimpleMaps().contains(mapName)) {
            this.map = MapLoader.loadSimpleMap(this, mapName);
        } else {
            throw new IllegalArgumentException("Unknown map name");
        }
        this.playerList = playerList;
        this.validateNewMap();
        this.currentPlayer = 0;
        this.turnCounter = 1;
    }

    /** @return the player data for the currently active player.*/
    public synchronized PlayerDTO getActivePlayer() {
        return this.playerList[this.currentPlayer];
    }
    
    /** @return the number of the current turn.*/
    public synchronized int getCurrentTurn() {
        return this.turnCounter;
    }

    /** @return the complete list of players who are allowed access to this engine.*/
    public synchronized PlayerDTO[] getAllPlayers() {
        return this.playerList;
    }

    /** @return the map that is currently loaded in the engine.*/
    public synchronized Map getMap() {
        return this.map;
    }

    /** @return the brief description of the map as seen from the perspective of the given player.
     * @param player - the player who is requesting the data.*/
    public synchronized MapDTO getMapDTO (@NotNull PlayerDTO player) {
        this.validatePlayer(player);
        return this.map.getDTO(player);
    }

    /** @return the brief description of the tile at given coordinates as seen from the perspective of the given player.
     * @param player - the player who is requesting the data.
     * @param location - the coordinates at which the queried tile is located.*/
    public synchronized TileDTO getTileDTO (@NotNull PlayerDTO player, @NotNull PointDTO location) {
        this.validatePlayer(player);
        this.validateLocation(location);
        return this.map.getTile(location).getDTO(player);
    }

    /** @return the brief description of the unit at given coordinates as seen from the perspective of the given player, or {@code null} if no unit exists there.
     * @param player - the player who is requesting the data.
     * @param location - the coordinates at which the queried unit is located.*/
    public synchronized UnitDTO getUnitDTO (@NotNull PlayerDTO player, @NotNull PointDTO location) {
        this.validatePlayer(player);
        this.validateLocation(location);
        Unit unit = this.map.getTile(location).getUnit();
        return (unit != null ? unit.getDTO(player) : null);
    }

    /** Cause the end of turn sequence on the map and relinquish control to another player.
     * @param player - the player who is finishing his turn.*/
    public synchronized void endTurn (@NotNull PlayerDTO player) {
        this.validatePlayer(player);
        if (player != this.getActivePlayer()) {
            return;
        }
        this.currentPlayer++;
        if (this.currentPlayer == this.playerList.length) {
            this.currentPlayer = 0;
            this.turnCounter++;
        }
        this.map.endTurn();
    }

    /** Cause a unit in the "from" location to perform the given action on the "to" location.
     * @param player - the player who is attempting to perform an action.
     * @param from - the location from which the action originates.
     * @param action - the index of the action which is to be performed.
     * @param to - the location which is meant to be influenced by this action.*/
    public synchronized void performAction (@NotNull PlayerDTO player, @NotNull PointDTO from, @NotNull Action action, @NotNull PointDTO to) {
        this.validatePlayer(player);
        this.validateLocation(to);
        this.validateLocation(from);
        Unit activeUnit = this.map.getTile(from).getUnit();
        if (player == this.getActivePlayer() && activeUnit != null) {
            this.ruleset.performAction(this.map, player, from, to, action);
        }
    }

    /** Select a unit for further instructions.
     * This pre-calculates some of the useful data, including but not limited to path finding,
     * while the player is (supposedly) contemplating his actions.
     * Calling this method is unnecessary but recommended for better performance.*/
    public synchronized void selectUnit (@NotNull PlayerDTO player, @NotNull PointDTO location) {
        this.validatePlayer(player);
        this.validateLocation(location);
        Unit activeUnit = this.map.getTile(location).getUnit();
        if (player == this.getActivePlayer() && activeUnit != null) {
            this.map.selectUnit(player, location);
        }
    }

    private void validateNewMap() {
        if (this.map.getMeta().getNumberOfPlayers() < this.playerList.length) {
            throw new IllegalArgumentException("There are more players than slots provided by the map.");
        }
        for (int x = 0; x < this.map.getMeta().getWidth(); x++) {
            for (int y = 0; y < this.map.getMeta().getHeight(); y++) {
                if (this.map.getTile(new PointDTO(x, y)) == null) {
                    throw new IllegalArgumentException("The loaded map is corrupted: some of the tiles are null.");
                }
            }
        }
    }

    private void validateLocation(PointDTO location) {
        if (location.getX() < 0 || location.getX() >= this.map.getMeta().getWidth() || location.getY() < 0 || location.getY() >= this.map.getMeta().getHeight()) {
            throw new IllegalArgumentException("The requested location is out of map bounds.");
        }
    }

    private void validatePlayer(PlayerDTO player) {
        boolean isPlayerValid = false;
        for (PlayerDTO validPlayer : this.playerList) {
            isPlayerValid |= (validPlayer == player);
        }
        if (!isPlayerValid) {
            throw new IllegalArgumentException("This player is not registered on this engine.");
        }
    }

    public void addListener(EngineListener engineListener, PlayerDTO playerDTO) {
        EngineMonitor.aspectOf().addListener(this, playerDTO, engineListener);
    }

    public void removeListener(EngineListener engineListener, PlayerDTO playerDTO) {
        EngineMonitor.aspectOf().addListener(this, playerDTO, engineListener);
    }
}
