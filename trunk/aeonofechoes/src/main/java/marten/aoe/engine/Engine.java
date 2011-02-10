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

import org.junit.Assert;

public final class Engine {
    private Map map;
    private final PlayerDTO[] playerList;
    private int currentPlayer = 0;
    private final List<EngineListener> listeners = new ArrayList<EngineListener>();
    public Engine (String mapName, PlayerDTO[] playerList) {
        this.map = MapLoader.loadMap(this, mapName);
        Assert.assertNotNull(this.map);
        this.playerList = playerList;
        Assert.assertNotNull(this.playerList);
        Assert.assertTrue(this.playerList.length > 0);
    }
    public synchronized void switchMap (String mapName) {
        this.map = MapLoader.loadMap(this, mapName);
        Assert.assertNotNull(this.map);
        this.invokeGlobalEvent(GlobalEvent.MAP_CHANGE);
    }
    public synchronized PlayerDTO getActivePlayer() {
        return this.playerList[this.currentPlayer];
    }
    public synchronized void addListener (EngineListener listener) {
        Assert.assertNotNull(listener);
        this.listeners.add(listener);
    }
    public synchronized void removeListener (EngineListener listener) {
        Assert.assertNotNull(listener);
        this.listeners.remove(listener);
    }
    public synchronized MapDTO getMapDTO (PlayerDTO player) {
        Assert.assertNotNull(player);
        return this.map.getDTO(player);
    }
    public synchronized FullMapDTO getFullMapDTO (PlayerDTO player) {
        Assert.assertNotNull(player);
        return this.map.getFullDTO(player);
    }
    public synchronized TileDTO getTileDTO (PlayerDTO player, PointDTO location) {
        Assert.assertNotNull(player);
        Assert.assertNotNull(location);
        Tile tile = this.map.getTile(location);
        Assert.assertNotNull(tile);
        return tile.getDTO(player);
    }
    public synchronized FullTileDTO getFullTileDTO (PlayerDTO player, PointDTO location) {
        Assert.assertNotNull(player);
        Assert.assertNotNull(location);
        Tile tile = this.map.getTile(location);
        Assert.assertNotNull(tile);
        return tile.getFullDTO(player);
    }
    public synchronized UnitDTO getUnitDTO (PlayerDTO player, PointDTO location) {
        Assert.assertNotNull(player);
        Assert.assertNotNull(location);
        Tile tile = this.map.getTile(location);
        Assert.assertNotNull(tile);
        Unit unit = tile.getUnit();
        return (unit != null ? unit.getDTO(player) : null);
    }
    public synchronized FullUnitDTO getFullUnitDTO (PlayerDTO player, PointDTO location) {
        Assert.assertNotNull(player);
        Assert.assertNotNull(location);
        Tile tile = this.map.getTile(location);
        Assert.assertNotNull(tile);
        Unit unit = tile.getUnit();
        return (unit != null ? unit.getFullDTO(player) : null);
    }
    @Deprecated public synchronized boolean moveUnit (PlayerDTO player, PointDTO from, PointDTO to) {
        // For testing purposes only. In normal circumstances the players should
        // rely on actions, defined in units, for movement.
        Assert.assertNotNull(player);
        Assert.assertNotNull(from);
        Assert.assertNotNull(to);
        return this.map.moveUnit(player, from, to);
    }
    @Deprecated public synchronized boolean createUnit (PlayerDTO player, String name, PointDTO at) {
        // For testing purposes only. In normal circumstances the players should
        // rely on buildings and/or map events to get new units.
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
    public synchronized void endTurn (PlayerDTO player) {
        Assert.assertNotNull(player);
        if (player != this.playerList[this.currentPlayer]) {
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
        Assert.assertNotNull(player);
        Assert.assertNotNull(actor);
        Assert.assertFalse(action < 1);
        Assert.assertFalse(action > 9);
        Assert.assertNotNull(target);
        if (player != this.playerList[this.currentPlayer]) {
            return;
        }
        Tile activeTile = this.map.getTile(actor);
        Assert.assertNotNull(activeTile);
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
        }
    }
    public void invokeLocalEvent(LocalEvent event, Tile location) {
        Assert.assertNotNull(event);
        Assert.assertNotNull(location);
        for (EngineListener listener : this.listeners) {
            if (location.isVisible(listener.getAssignedPlayer())) {
                listener.onLocalEvent(event, location.getFullDTO(listener.getAssignedPlayer()));
            }
        }
    }
    public void invokeGlobalEvent(GlobalEvent event) {
        Assert.assertNotNull(event);
        for (EngineListener listener : this.listeners) {
            listener.onGlobalEvent(event);
        }
    }
}
