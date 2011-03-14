package marten.aoe.engine.aspectj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.aspectj.lang.annotation.SuppressAjWarnings;

import marten.aoe.aspectj.NotNull;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.engine.Engine;
import marten.aoe.engine.EngineListener;
import marten.aoe.engine.GlobalEvent;
import marten.aoe.engine.LocalEvent;
import marten.aoe.engine.Map;
import marten.aoe.engine.Tile;
import marten.aoe.engine.TileBase;
import marten.aoe.engine.Unit;

@SuppressAjWarnings("adviceDidNotMatch")
public final aspect EngineMonitor {
    
    // INJECTED FIELDS
    private final java.util.Map<PlayerDTO, List<EngineListener>> Engine.listeners = new HashMap<PlayerDTO, List<EngineListener>>();
    
    // POINTCUTS
    pointcut onMapReloaded (Engine engine) :
        target(engine) && (execution(* Engine.switchMap(..)));
    
    pointcut onEngineInitiated (Engine engine) :
        this(engine) && execution(Engine.new(..));
    
    pointcut onTurnEnd (Engine engine) :
        target(engine) && execution(* Engine.endTurn(..));
    
    pointcut onTileSwitch (Map map, Tile tile) :
        target(map) && args(tile) && execution(* Map.switchTile(Tile));
    
    pointcut onUnitDamaged (Unit unit) :
        target(unit) && execution(* Unit.applyDamage(..));
    
    pointcut onUnitEntry (TileBase tile) :
        target(tile) && (execution(* TileBase.pushUnit(..)) || execution(* TileBase.insertUnit(..)));
    
    pointcut onUnitExit (TileBase tile) :
        target(tile) && execution(* TileBase.removeUnit(..));

    // PUBLIC METHODS
    public void addListener (@NotNull Engine engine, @NotNull PlayerDTO player, @NotNull EngineListener listener) {
        if (!engine.listeners.containsKey(player)) {
            throw new IllegalArgumentException("This player is not registered on this engine.");
        }
        engine.listeners.get(player).add(listener);
        listener.onGlobalEvent(GlobalEvent.MAP_INITIATED);
    }
    
    public void removeListener (@NotNull Engine engine, @NotNull PlayerDTO player, @NotNull EngineListener listener) {
        if (!engine.listeners.containsKey(player)) {
            throw new IllegalArgumentException("This player is not registered on this engine.");
        }
        engine.listeners.get(player).remove(listener);
        listener.onGlobalEvent(GlobalEvent.MAP_TERMINATED);
    }
    
    // ADVICE
    after (Engine engine) returning() : onEngineInitiated(engine) {
        for (PlayerDTO player : engine.getAllPlayers()) {
            engine.listeners.put(player, new ArrayList<EngineListener>());
        }
    }
    
    after (Engine engine) : onMapReloaded(engine) {
        for (PlayerDTO player : engine.getAllPlayers()) {
            if (!engine.listeners.containsKey(player)) {
                engine.listeners.put(player, new ArrayList<EngineListener>());
            }
        }
        for (PlayerDTO listeningPlayer : engine.listeners.keySet()) {
            boolean contains = false;
            for (PlayerDTO player : engine.getAllPlayers()) {
                contains |= (player == listeningPlayer);
            }
            if (contains) {
                for (EngineListener listener : engine.listeners.get(listeningPlayer)) {
                    listener.onGlobalEvent(GlobalEvent.MAP_CHANGE);
                }
            }
            else {
                for (EngineListener listener : engine.listeners.get(listeningPlayer)) {
                    listener.onGlobalEvent(GlobalEvent.MAP_TERMINATED);
                }
                engine.listeners.remove(listeningPlayer);
            }
        }
    }
    
    void around (Engine engine) : onTurnEnd(engine) {
        PlayerDTO activePlayer = engine.getActivePlayer();
        proceed(engine);
        if (activePlayer != engine.getActivePlayer()) {
            for (List<EngineListener> listeners : engine.listeners.values()) {
                for (EngineListener listener : listeners) {
                    listener.onGlobalEvent(GlobalEvent.TURN_END);
                }
            }
        }
    }
    
    after(Map map, Tile tile) : onTileSwitch(map, tile) {
        Engine engine = map.getOwner();
        for (PlayerDTO player : engine.listeners.keySet()) {
            if (tile.isVisible(player)) {
                for (EngineListener listener : engine.listeners.get(player)) {
                    listener.onLocalEvent(LocalEvent.TILE_CHANGE, tile.getDTO(player));
                }
            }
        }
    }
    
    void around (Unit unit) : onUnitDamaged(unit) {
        Engine engine = unit.getMap().getOwner();
        Tile tile = unit.getLocation();
        int health = unit.getHitPoints();
        proceed(unit);
        if (unit.getHitPoints() < 0) {
            for (PlayerDTO player : engine.listeners.keySet()) {
                if (tile.isVisible(player)) {
                    for (EngineListener listener : engine.listeners.get(player)) {
                        listener.onLocalEvent(LocalEvent.UNIT_DEAD, tile.getDTO(player));
                    }
                }
            }
            unit.getMap().recalculateVisibility(unit.getOwner());
        }
        else if (unit.getHitPoints() < health) {
            for (PlayerDTO player : engine.listeners.keySet()) {
                if (tile.isVisible(player)) {
                    for (EngineListener listener : engine.listeners.get(player)) {
                        listener.onLocalEvent(LocalEvent.UNIT_HURT, tile.getDTO(player));
                    }
                }
            }
        }
    }
    
    after (Tile tile) returning (boolean success) : onUnitEntry(tile) {
        Engine engine = tile.getMap().getOwner();
        if (success) {
            for (PlayerDTO player : engine.listeners.keySet()) {
                if (tile.isVisible(player)) {
                    for (EngineListener listener : engine.listeners.get(player)) {
                        listener.onLocalEvent(LocalEvent.UNIT_ENTRY, tile.getDTO(player));
                    }
                }
            }
        }
    }
    
    after (Tile tile) returning (Unit unit) : onUnitExit(tile) {
        Engine engine = tile.getMap().getOwner();
        if (unit != null) {
            for (PlayerDTO player : engine.listeners.keySet()) {
                if (tile.isVisible(player)) {
                    for (EngineListener listener : engine.listeners.get(player)) {
                        listener.onLocalEvent(LocalEvent.UNIT_EXIT, tile.getDTO(player));
                    }
                }
            }
        }
    }
}
