package marten.aoe.engine.aspectj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.aspectj.lang.annotation.SuppressAjWarnings;

import marten.aoe.aspectj.NotNull;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
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
    
    pointcut onUnitActing (Unit unit) :
        target(unit) && execution(* Unit.specialAction(..));
    
    pointcut onUnitEntry (TileBase tile) :
        target(tile) && (execution(* TileBase.pushUnit(..)) || execution(* TileBase.insertUnit(..)));
    
    pointcut onUnitExit (TileBase tile) :
        target(tile) && execution(* TileBase.removeUnit(..));
    
    pointcut onTileExplored (TileBase tile, PlayerDTO player) :
        target(tile) && args(player) && execution(* TileBase.setExplored(..));
    
    pointcut onTileVisible (TileBase tile, PlayerDTO player) :
        target(tile) && args(player) && execution(* TileBase.setVisible(..));
    
    pointcut onTileInvisible (TileBase tile, PlayerDTO player) :
        target(tile) && args(player) && execution(* TileBase.setInvisible(..));
    
    pointcut onObjectDetected (TileBase tile, PlayerDTO player) :
        target(tile) && args(player) && execution(* TileBase.setDetected(..));
    
    pointcut onObjectHidden (TileBase tile, PlayerDTO player) :
        target(tile) && args(player) && execution(* TileBase.setUndetected(..));
    
    // FIXME: this pointcut shall apply to Map class instead of Engine in the nearest future.
    pointcut onUnitSpawned (Engine engine, PlayerDTO player, PointDTO location) :
        target(engine) && args(player, *, location) && execution(* Engine.spawnUnit(..));
        

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
                TileDTO tileData = map.getTile(tile.getCoordinates()).getDTO(player);
                for (EngineListener listener : engine.listeners.get(player)) {
                    listener.onLocalEvent(LocalEvent.TILE_CHANGE, tileData);
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
                    TileDTO tileData = unit.getMap().getTile(tile.getCoordinates()).getDTO(player);
                    for (EngineListener listener : engine.listeners.get(player)) {
                        listener.onLocalEvent(LocalEvent.UNIT_DEAD, tileData);
                    }
                }
            }
        }
        else if (unit.getHitPoints() < health) {
            for (PlayerDTO player : engine.listeners.keySet()) {
                if (tile.isVisible(player)) {
                    TileDTO tileData = unit.getMap().getTile(tile.getCoordinates()).getDTO(player);
                    for (EngineListener listener : engine.listeners.get(player)) {
                        listener.onLocalEvent(LocalEvent.UNIT_HURT, tileData);
                    }
                }
            }
        }
    }
    
    after (Tile tile) returning (boolean success) : onUnitEntry(tile) {
        Engine engine = tile.getMap().getOwner();
        if (success) {
            Unit unit = tile.getUnit();
            for (PlayerDTO player : engine.listeners.keySet()) {
                if (unit.isDetected(player)) {
                    TileDTO tileData = tile.getMap().getTile(tile.getCoordinates()).getDTO(player);                    
                    for (EngineListener listener : engine.listeners.get(player)) {
                        listener.onLocalEvent(LocalEvent.UNIT_ENTRY, tileData);
                    }
                }
            }
        }
    }
    
    after (Tile tile) returning (Unit unit) : onUnitExit(tile) {
        Engine engine = tile.getMap().getOwner();
        if (unit != null) {
            for (PlayerDTO player : engine.listeners.keySet()) {
                if (unit.isDetected(player)) {
                    for (EngineListener listener : engine.listeners.get(player)) {
                        listener.onLocalEvent(LocalEvent.UNIT_EXIT, tile.getDTO(player));
                    }
                }
            }
        }
    }
    
    void around (Unit unit) : onUnitActing(unit) {
        Engine engine = unit.getMap().getOwner();
        for (List<EngineListener> listeners : engine.listeners.values()) {
            for (EngineListener listener : listeners) {
                listener.onGlobalEvent(GlobalEvent.STREAM_START);
            }
        }
        proceed(unit);
        for (PlayerDTO player : engine.getAllPlayers()) {
            engine.getMap().recalculateVisibility(player);
        }
        for (List<EngineListener> listeners : engine.listeners.values()) {
            for (EngineListener listener : listeners) {
                listener.onGlobalEvent(GlobalEvent.STREAM_END);
            }
        }        
    }
    
    after (Tile tile, PlayerDTO player) : onTileExplored(tile, player) {
        Engine engine = tile.getMap().getOwner();
        TileDTO tileData = tile.getMap().getTile(tile.getCoordinates()).getDTO(player);        
        for (EngineListener listener : engine.listeners.get(player)) {
            listener.onLocalEvent(LocalEvent.TILE_EXPLORED, tileData);
        }
    }
    
    after (Tile tile, PlayerDTO player) : onTileVisible(tile, player) {
        Engine engine = tile.getMap().getOwner();
        TileDTO tileData = tile.getMap().getTile(tile.getCoordinates()).getDTO(player);        
        for (EngineListener listener : engine.listeners.get(player)) {
            listener.onLocalEvent(LocalEvent.TILE_VISIBLE, tileData);
        }
    }
    
    after (Tile tile, PlayerDTO player) : onTileInvisible(tile, player) {
        Engine engine = tile.getMap().getOwner();
        TileDTO tileData = tile.getMap().getTile(tile.getCoordinates()).getDTO(player);        
        for (EngineListener listener : engine.listeners.get(player)) {
            listener.onLocalEvent(LocalEvent.TILE_INVISIBLE, tileData);
        }
    }
    
    after (Tile tile, PlayerDTO player) : onObjectDetected(tile, player) {
        if (tile.isVisible(player)) {
            Engine engine = tile.getMap().getOwner();
            TileDTO tileData = tile.getMap().getTile(tile.getCoordinates()).getDTO(player);        
            for (EngineListener listener : engine.listeners.get(player)) {
                listener.onLocalEvent(LocalEvent.OBJECT_DETECTED, tileData);
            }
        }
    }
    
    after (Tile tile, PlayerDTO player) : onObjectHidden(tile, player) {
        if (tile.isVisible(player)) {
            Engine engine = tile.getMap().getOwner();
            TileDTO tileData = tile.getMap().getTile(tile.getCoordinates()).getDTO(player);        
            for (EngineListener listener : engine.listeners.get(player)) {
                listener.onLocalEvent(LocalEvent.OBJECT_CLOAKED, tileData);
            }
        }
    }    
    
    after (Engine engine, PlayerDTO player, PointDTO location) : onUnitSpawned(engine, player, location) {
        TileDTO tileData = engine.getMap().getTile(location).getDTO(player);        
        for (EngineListener listener : engine.listeners.get(player)) {
            listener.onLocalEvent(LocalEvent.UNIT_ENTRY, tileData);
        }
    }
}
