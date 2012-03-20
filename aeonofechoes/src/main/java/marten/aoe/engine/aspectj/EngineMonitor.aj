package marten.aoe.engine.aspectj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    // INJECTED FIELDS
    private final java.util.Map<Player, List<EngineListener>> Engine.listeners = new HashMap<Player, List<EngineListener>>();

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

    // PUBLIC METHODS
    public void addListener(@NotNull Engine engine, @NotNull Player player,
            @NotNull EngineListener listener) {
        if (!engine.listeners.containsKey(player)) {
            throw new IllegalArgumentException(
                    "This player is not registered on this engine.");
        }
        engine.listeners.get(player).add(listener);
        listener.onGlobalEvent(GlobalEvent.MAP_INITIATED);
    }

    public void removeListener(@NotNull Engine engine, @NotNull Player player,
            @NotNull EngineListener listener) {
        if (!engine.listeners.containsKey(player)) {
            throw new IllegalArgumentException(
                    "This player is not registered on this engine.");
        }
        engine.listeners.get(player).remove(listener);
        listener.onGlobalEvent(GlobalEvent.MAP_TERMINATED);
    }

    // ADVICE
    after(Engine engine) returning() : onEngineInitiated(engine) {
        for (Player player : engine.getAllPlayers()) {
            engine.listeners.put(player, new ArrayList<EngineListener>());
        }
    }

    after(Engine engine) : onMapReloaded(engine) {
        for (Player player : engine.getAllPlayers()) {
            if (!engine.listeners.containsKey(player)) {
                engine.listeners.put(player, new ArrayList<EngineListener>());
            }
        }
        for (Player listeningPlayer : engine.listeners.keySet()) {
            boolean contains = false;
            for (Player player : engine.getAllPlayers()) {
                contains |= (player == listeningPlayer);
            }
            if (contains) {
                for (EngineListener listener : engine.listeners
                        .get(listeningPlayer)) {
                    listener.onGlobalEvent(GlobalEvent.MAP_CHANGE);
                }
            } else {
                for (EngineListener listener : engine.listeners
                        .get(listeningPlayer)) {
                    listener.onGlobalEvent(GlobalEvent.MAP_TERMINATED);
                }
                engine.listeners.remove(listeningPlayer);
            }
        }
    }

    void around(Engine engine) : onTurnEnd(engine) {
        Player activePlayer = engine.getActivePlayer();
        int currentTurn = engine.getCurrentTurn();
        for (List<EngineListener> listeners : engine.listeners.values()) {
            for (EngineListener listener : listeners) {
                listener.onGlobalEvent(GlobalEvent.STREAM_START);
            }
        }
        proceed(engine);
        if (activePlayer != engine.getActivePlayer()
                || currentTurn != engine.getCurrentTurn()) {
            for (List<EngineListener> listeners : engine.listeners.values()) {
                for (EngineListener listener : listeners) {
                    listener.onGlobalEvent(GlobalEvent.TURN_END);
                }
            }
        }
        for (List<EngineListener> listeners : engine.listeners.values()) {
            for (EngineListener listener : listeners) {
                listener.onGlobalEvent(GlobalEvent.STREAM_END);
            }
        }
    }

    after(Unit unit) : onUnitTurnEnd(unit) {
        Engine engine = unit.getMap().getOwner();
        for (Player player : engine.listeners.keySet()) {
            if (unit.getLocation().isVisible(player)) {
                TileDTO tileData = unit.getMap()
                        .getTile(unit.getLocation().getCoordinates())
                        .getDTO(player);
                for (EngineListener listener : engine.listeners.get(player)) {
                    listener.onLocalEvent(LocalEvent.UNIT_REFRESH, tileData);
                }
            }
        }
    }

    after(Map map, Tile tile) : onTileSwitch(map, tile) {
        Engine engine = map.getOwner();
        for (Player player : engine.listeners.keySet()) {
            if (tile.isVisible(player)) {
                TileDTO tileData = map.getTile(tile.getCoordinates()).getDTO(
                        player);
                for (EngineListener listener : engine.listeners.get(player)) {
                    listener.onLocalEvent(LocalEvent.TILE_CHANGE, tileData);
                }
            }
        }
    }

    after(Tile tile) returning (boolean success) : onUnitEntry(tile) {
        Engine engine = tile.getMap().getOwner();
        if (success) {
            Unit unit = tile.getUnit();
            for (Player player : engine.listeners.keySet()) {
                if (unit.isDetected(player)) {
                    TileDTO tileData = tile.getMap()
                            .getTile(tile.getCoordinates()).getDTO(player);
                    for (EngineListener listener : engine.listeners.get(player)) {
                        listener.onLocalEvent(LocalEvent.UNIT_ENTRY, tileData);
                    }
                }
            }
        }
    }

    after(Tile tile) returning (Unit unit) : onUnitExit(tile) {
        Engine engine = tile.getMap().getOwner();
        if (unit != null) {
            for (Player player : engine.listeners.keySet()) {
                if (unit.isDetected(player)) {
                    TileDTO tileData = tile.getMap()
                            .getTile(tile.getCoordinates()).getDTO(player);
                    for (EngineListener listener : engine.listeners.get(player)) {
                        listener.onLocalEvent(LocalEvent.UNIT_EXIT, tileData);
                    }
                }
            }
        }
    }

    void around(Engine engine) : onUnitActing(engine) {
        for (List<EngineListener> listeners : engine.listeners.values()) {
            for (EngineListener listener : listeners) {
                listener.onGlobalEvent(GlobalEvent.STREAM_START);
            }
        }
        proceed(engine);
        for (Player player : engine.getAllPlayers()) {
            engine.getMap().recalculateVisibility(player);
        }
        for (List<EngineListener> listeners : engine.listeners.values()) {
            for (EngineListener listener : listeners) {
                listener.onGlobalEvent(GlobalEvent.STREAM_END);
            }
        }
    }

    after(Tile tile, Player player) : onTileExplored(tile, player) {
        Engine engine = tile.getMap().getOwner();
        TileDTO tileData = tile.getMap().getTile(tile.getCoordinates())
                .getDTO(player);
        for (EngineListener listener : engine.listeners.get(player)) {
            listener.onLocalEvent(LocalEvent.TILE_EXPLORED, tileData);
        }
    }

    after(Tile tile, Player player) : onTileVisible(tile, player) {
        Engine engine = tile.getMap().getOwner();
        TileDTO tileData = tile.getMap().getTile(tile.getCoordinates())
                .getDTO(player);
        for (EngineListener listener : engine.listeners.get(player)) {
            listener.onLocalEvent(LocalEvent.TILE_VISIBLE, tileData);
        }
    }

    after(Tile tile, Player player) : onTileInvisible(tile, player) {
        Engine engine = tile.getMap().getOwner();
        TileDTO tileData = tile.getMap().getTile(tile.getCoordinates())
                .getDTO(player);
        for (EngineListener listener : engine.listeners.get(player)) {
            listener.onLocalEvent(LocalEvent.TILE_INVISIBLE, tileData);
        }
    }

    after(Tile tile, Player player) : onTilePowered(tile, player) {
        Engine engine = tile.getMap().getOwner();
        TileDTO tileData = tile.getMap().getTile(tile.getCoordinates())
                .getDTO(player);
        for (EngineListener listener : engine.listeners.get(player)) {
            listener.onLocalEvent(LocalEvent.TILE_POWERED, tileData);
        }
    }

    after(Tile tile, Player player) : onTileUnpowered(tile, player) {
        Engine engine = tile.getMap().getOwner();
        TileDTO tileData = tile.getMap().getTile(tile.getCoordinates())
                .getDTO(player);
        for (EngineListener listener : engine.listeners.get(player)) {
            listener.onLocalEvent(LocalEvent.TILE_UNPOWERED, tileData);
        }
    }

    after(Tile tile, Player player) : onObjectDetected(tile, player) {
        if (tile.isVisible(player)) {
            Engine engine = tile.getMap().getOwner();
            TileDTO tileData = tile.getMap().getTile(tile.getCoordinates())
                    .getDTO(player);
            for (EngineListener listener : engine.listeners.get(player)) {
                listener.onLocalEvent(LocalEvent.OBJECT_DETECTED, tileData);
            }
        }
    }

    after(Tile tile, Player player) : onObjectHidden(tile, player) {
        if (tile.isVisible(player)) {
            Engine engine = tile.getMap().getOwner();
            TileDTO tileData = tile.getMap().getTile(tile.getCoordinates())
                    .getDTO(player);
            for (EngineListener listener : engine.listeners.get(player)) {
                listener.onLocalEvent(LocalEvent.OBJECT_CLOAKED, tileData);
            }
        }
    }

    before(Engine engine) : onEngineStart(engine) {
        for (Player p : engine.getAllPlayers()) {
            for (EngineListener listener : engine.listeners.get(p)) {
                listener.onGlobalEvent(GlobalEvent.STREAM_START);
            }
        }
    }

    after(Engine engine) : onEngineStart(engine) {
        for (Player p : engine.getAllPlayers()) {
            for (EngineListener listener : engine.listeners.get(p)) {
                listener.onGlobalEvent(GlobalEvent.STREAM_END);
            }
        }
    }

    after(Map map, Player player, PointDTO location) : onUnitSpawned(map, player, location) {
        TileDTO tileData = map.getTile(location).getDTO(player);
        for (EngineListener listener : map.getOwner().listeners.get(player)) {
            listener.onLocalEvent(LocalEvent.UNIT_ENTRY, tileData);
        }
        for (Player p : map.getOwner().getAllPlayers()) {
            map.recalculateVisibility(p);
        }
    }

    after(Player player) : onSetMoney(player) {
        for (EngineListener listener : player.getOwner().listeners.get(player)) {
            listener.onGlobalEvent(GlobalEvent.PLAYER_REFRESH);
        }
    }

    after(Unit unit) : onApplyDamage(unit) {
        if (unit.getLocation() != null) {
            for (EngineListener listener : unit.getOwner().getOwner().listeners
                    .get(unit.getOwner())) {
                listener.onLocalEvent(LocalEvent.UNIT_HURT, unit.getLocation()
                        .getDTO(unit.getOwner()));
            }
        }
    }
}
