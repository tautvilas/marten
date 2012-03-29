package marten.aoe.engine.aspectj;

import java.util.HashMap;

import marten.aoe.aspectj.NotNull;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.engine.Engine;
import marten.aoe.engine.EngineListener;
import marten.aoe.engine.GlobalEvent;
import marten.aoe.engine.LocalEvent;
import marten.aoe.engine.core.Map;
import marten.aoe.engine.core.Player;
import marten.aoe.engine.core.Tile;
import marten.aoe.engine.core.Unit;

import org.aspectj.lang.annotation.SuppressAjWarnings;

@SuppressAjWarnings("adviceDidNotMatch")
public final aspect EngineMonitor {

    private final java.util.Map<Player, EngineListener> listeners = new HashMap<Player, EngineListener>();

    // POINTCUTS
    pointcut onMapReloaded(Engine engine) :
        target(engine) && (execution(* Engine.switchMap(..)));

    pointcut onEngineInitiated(Engine engine) :
        this(engine) && execution(Engine.new(..));

    pointcut onTurnEnd(Engine engine) :
        target(engine) && execution(* Engine.endTurn(..));

    pointcut onUnitTurnEnd(Unit unit) :
        target(unit) && execution(* Unit.turnOver(..));

    pointcut onTileSwitch(Map map, Tile tile) :
        target(map) && args(tile) && execution(* Map.switchTile(Tile));

    pointcut onUnitActing(Engine engine) :
        target(engine) && execution(* Engine.performAction(..));

    pointcut onUnitEntry(Tile tile) :
        target(tile) && (execution(* Tile.pushUnit(..)) || execution(* Tile.insertUnit(..)));

    pointcut onUnitExit(Tile tile) :
        target(tile) && execution(* Tile.removeUnit(..));

    pointcut onTileExplored(Tile tile, Player player) :
        target(tile) && args(player) && execution(* Tile.setExplored(..));

    pointcut onTileVisible(Tile tile, Player player) :
        target(tile) && args(player) && execution(* Tile.setVisible(..));

    pointcut onTilePowered(Tile tile, Player player) :
        target(tile) && args(player) && execution(* Tile.setPowered(..));

    pointcut onTileUnpowered(Tile tile, Player player) :
        target(tile) && args(player) && execution(* Tile.setUnpowered(..));

    pointcut onTileInvisible(Tile tile, Player player) :
        target(tile) && args(player) && execution(* Tile.setInvisible(..));

    pointcut onObjectDetected(Tile tile, Player player) :
        target(tile) && args(player) && execution(* Tile.setDetected(..));

    pointcut onObjectHidden(Tile tile, Player player) :
        target(tile) && args(player) && execution(* Tile.setUndetected(..));

    pointcut onUnitSpawned(Map map, Player player, PointDTO location) :
        target(map) && args(player, *, location) && execution(* Map.spawnUnit(..));

    pointcut onEngineStart(Engine engine) :
        target(engine) && args() && execution(* Engine.start());

    pointcut onSetMoney(Player player) :
        target(player) && args(*) && execution(* Player.setMoney(..));

    pointcut onApplyDamage(Unit unit) :
        target(unit) && args(*) && execution(* Unit.applyDamage(..));

    pointcut onDie(Unit unit) :
        target(unit) && args() && execution(* Unit.die());

    // PUBLIC METHODS
    public void addListener(@NotNull Engine engine, @NotNull Player player,
            @NotNull EngineListener listener) {
        listeners.put(player, listener);
        listener.onGlobalEvent(GlobalEvent.MAP_INITIATED);
    }

    public void removeListener(@NotNull Engine engine, @NotNull Player player) {
        listeners.get(player).onGlobalEvent(GlobalEvent.MAP_TERMINATED);
        listeners.remove(player);
    }

    // ADVICE
    after(Engine engine) returning() : onEngineInitiated(engine) {
    }

    after(Engine engine) : onMapReloaded(engine) {
        for (Player listeningPlayer : listeners.keySet()) {
            boolean contains = false;
            for (Player player : engine.getAllPlayers()) {
                contains |= (player == listeningPlayer);
            }
            if (contains) {
                listeners.get(listeningPlayer).onGlobalEvent(
                        GlobalEvent.MAP_CHANGE);
            } else {
                listeners.get(listeningPlayer).onGlobalEvent(
                        GlobalEvent.MAP_TERMINATED);
                listeners.remove(listeningPlayer);
            }
        }
    }

    void around(Engine engine) : onTurnEnd(engine) {
        Player activePlayer = engine.getActivePlayer();
        int currentTurn = engine.getCurrentTurn();
        for (EngineListener listener : listeners.values()) {
            listener.onGlobalEvent(GlobalEvent.STREAM_START);
        }
        proceed(engine);
        if (activePlayer != engine.getActivePlayer()
                || currentTurn != engine.getCurrentTurn()) {
            for (EngineListener listener : listeners.values()) {
                listener.onGlobalEvent(GlobalEvent.TURN_END);
            }
        }
        for (EngineListener listener : listeners.values()) {
            listener.onGlobalEvent(GlobalEvent.STREAM_END);
        }
    }

    after(Unit unit) : onUnitTurnEnd(unit) {
        for (Player player : listeners.keySet()) {
            if (unit.getLocation().isVisible(player)) {
                TileDTO tileData = unit.getMap()
                        .getTile(unit.getLocation().getCoordinates())
                        .getDTO(player);
                listeners.get(player).onLocalEvent(LocalEvent.UNIT_REFRESH,
                        tileData);
            }
        }
    }

    after(Map map, Tile tile) : onTileSwitch(map, tile) {
        for (Player player : listeners.keySet()) {
            if (tile.isVisible(player)) {
                TileDTO tileData = map.getTile(tile.getCoordinates()).getDTO(
                        player);
                listeners.get(player).onLocalEvent(LocalEvent.TILE_CHANGE,
                        tileData);
            }
        }
    }

    after(Tile tile) returning (boolean success) : onUnitEntry(tile) {
        if (success) {
            Unit unit = tile.getUnit();
            for (Player player : listeners.keySet()) {
                if (unit.isDetected(player)) {
                    TileDTO tileData = tile.getMap()
                            .getTile(tile.getCoordinates()).getDTO(player);
                    listeners.get(player).onLocalEvent(LocalEvent.UNIT_ENTRY,
                            tileData);
                }
            }
        }
    }

    after(Tile tile) returning (Unit unit) : onUnitExit(tile) {
        if (unit != null) {
            for (Player player : listeners.keySet()) {
                if (unit.isDetected(player)) {
                    TileDTO tileData = tile.getMap()
                            .getTile(tile.getCoordinates()).getDTO(player);
                    listeners.get(player).onLocalEvent(LocalEvent.UNIT_EXIT,
                            tileData);
                }
            }
        }
    }

    void around(Engine engine) : onUnitActing(engine) {
        for (EngineListener listener : listeners.values()) {
            listener.onGlobalEvent(GlobalEvent.STREAM_START);
        }
        proceed(engine);
        for (Player player : engine.getAllPlayers()) {
            engine.getMap().recalculateVisibility(player);
        }
        for (EngineListener listener : listeners.values()) {
            listener.onGlobalEvent(GlobalEvent.STREAM_END);
        }
    }

    after(Tile tile, Player player) : onTileExplored(tile, player) {
        TileDTO tileData = tile.getMap().getTile(tile.getCoordinates())
                .getDTO(player);
        listeners.get(player).onLocalEvent(LocalEvent.TILE_EXPLORED, tileData);
    }

    after(Tile tile, Player player) : onTileVisible(tile, player) {
        TileDTO tileData = tile.getMap().getTile(tile.getCoordinates())
                .getDTO(player);
        listeners.get(player).onLocalEvent(LocalEvent.TILE_VISIBLE, tileData);
    }

    after(Tile tile, Player player) : onTileInvisible(tile, player) {
        TileDTO tileData = tile.getMap().getTile(tile.getCoordinates())
                .getDTO(player);
        listeners.get(player).onLocalEvent(LocalEvent.TILE_INVISIBLE, tileData);
    }

    after(Tile tile, Player player) : onTilePowered(tile, player) {
        TileDTO tileData = tile.getMap().getTile(tile.getCoordinates())
                .getDTO(player);
        listeners.get(player).onLocalEvent(LocalEvent.TILE_POWERED, tileData);
    }

    after(Tile tile, Player player) : onTileUnpowered(tile, player) {
        TileDTO tileData = tile.getMap().getTile(tile.getCoordinates())
                .getDTO(player);
        listeners.get(player).onLocalEvent(LocalEvent.TILE_UNPOWERED, tileData);
    }

    after(Tile tile, Player player) : onObjectDetected(tile, player) {
        if (tile.isVisible(player)) {
            TileDTO tileData = tile.getMap().getTile(tile.getCoordinates())
                    .getDTO(player);
            listeners.get(player).onLocalEvent(LocalEvent.OBJECT_DETECTED,
                    tileData);
        }
    }

    after(Tile tile, Player player) : onObjectHidden(tile, player) {
        if (tile.isVisible(player)) {
            TileDTO tileData = tile.getMap().getTile(tile.getCoordinates())
                    .getDTO(player);
            listeners.get(player).onLocalEvent(LocalEvent.OBJECT_CLOAKED,
                    tileData);
        }
    }

    before(Engine engine) : onEngineStart(engine) {
        for (Player p : engine.getAllPlayers()) {
            listeners.get(p).onGlobalEvent(GlobalEvent.STREAM_START);
        }
    }

    after(Engine engine) : onEngineStart(engine) {
        for (Player p : engine.getAllPlayers()) {
            listeners.get(p).onGlobalEvent(GlobalEvent.STREAM_END);
        }
    }

    after(Map map, Player player, PointDTO location) : onUnitSpawned(map, player, location) {
        TileDTO tileData = map.getTile(location).getDTO(player);
        listeners.get(player).onLocalEvent(LocalEvent.UNIT_ENTRY, tileData);
        for (Player p : map.getOwner().getAllPlayers()) {
            map.recalculateVisibility(p);
        }
    }

    after(Player player) : onSetMoney(player) {
        listeners.get(player).onGlobalEvent(GlobalEvent.PLAYER_REFRESH);
    }

    after(Unit unit) : onApplyDamage(unit) {
        if (unit.getLocation() != null) {
            listeners.get(unit.getOwner()).onLocalEvent(LocalEvent.UNIT_HURT,
                    unit.getLocation().getDTO(unit.getOwner()));
        }
    }

    before(Unit unit) : onDie(unit) {
        Engine engine = unit.getOwner().getOwner();
        for (Player p : engine.getAllPlayers()) {
            if (unit.isDetected(p)) {
                listeners.get(p).onLocalEvent(LocalEvent.UNIT_DEAD,
                        unit.getLocation().getDTO(unit.getOwner()));
            }
        }
    }
}
