package marten.aoe.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

import marten.aoe.dto.Action;
import marten.aoe.dto.MapDTO;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.engine.Engine;
import marten.aoe.engine.EngineListener;
import marten.aoe.engine.GlobalEvent;
import marten.aoe.engine.LocalEvent;
import marten.aoe.server.face.EngineFace;
import marten.aoe.server.serializable.EngineEvent;

import org.apache.log4j.Logger;

public class EngineInterface extends UnicastRemoteObject implements EngineFace {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(EngineFace.class);

    private final Engine engine;
    private final PlayerDTO player;
    private final LinkedList<EngineEvent> events = new LinkedList<EngineEvent>();
    private final LinkedList<LinkedList<TileDTO>> streams = new LinkedList<LinkedList<TileDTO>>();
    private final LinkedList<TileDTO> tiles = new LinkedList<TileDTO>();
    private boolean streaming = false;
    private static final long serialVersionUID = 1L;

    public EngineInterface(Engine engine, PlayerDTO player)
            throws RemoteException {
        super();
        this.engine = engine;
        this.player = player;
        this.engine.addListener(new EngineListener() {

            @Override
            public void onLocalEvent(LocalEvent event, TileDTO location) {
                // EngineInterface.log.info(EngineInterface.this.player.getName()
                // + " " + event + ", " + location);
                if (event == LocalEvent.TILE_EXPLORED
                        || event == LocalEvent.UNIT_ENTRY
                        || event == LocalEvent.UNIT_EXIT
                        || event == LocalEvent.TILE_INVISIBLE
                        || event == LocalEvent.TILE_VISIBLE) {
                    synchronized (EngineInterface.this.tiles) {
                        EngineInterface.this.tiles.add(location);
                    }
                    if (!EngineInterface.this.streaming) {
                        synchronized (EngineInterface.this.events) {
                            EngineInterface.this.events
                                    .add(EngineEvent.TILE_UPDATE);
                            EngineInterface.this.events.notifyAll();
                        }
                    }
                } else {
                    EngineInterface.log.info(EngineInterface.this.player
                            .getName()
                            + " " + event);
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onGlobalEvent(GlobalEvent event) {
                if (event == GlobalEvent.TURN_END) {
                    synchronized (EngineInterface.this.events) {
                        EngineInterface.this.events.add(EngineEvent.TURN_END);
                        EngineInterface.this.events.notifyAll();
                    }
                } else if (event == GlobalEvent.STREAM_START) {
                    EngineInterface.this.streaming = true;
                } else if (event == GlobalEvent.STREAM_END) {
                    synchronized (EngineInterface.this.tiles) {
                        synchronized (EngineInterface.this.streams) {
                            EngineInterface.this.streams
                                    .add((LinkedList<TileDTO>)EngineInterface.this.tiles
                                            .clone());
                        }
                        synchronized (EngineInterface.this.events) {
                            EngineInterface.this.events
                                    .add(EngineEvent.STREAM_UPDATE);
                            EngineInterface.this.events.notifyAll();
                        }
                    }
                    EngineInterface.this.streaming = false;
                } else {
                    EngineInterface.log.info(EngineInterface.this.player
                            .getName()
                            + " " + event);
                }
            }
        }, this.player);
    }

    @Override
    public MapDTO getMap() throws RemoteException {
        return this.engine.getMapDTO(this.player);
    }

    @Deprecated
    @Override
    public synchronized void moveUnit(PointDTO from, PointDTO to)
            throws RemoteException {
        this.engine.performAction(this.player, from, Action.FIRST, to);
    }

    @Deprecated
    @Override
    public synchronized boolean createUnit(String name, PointDTO at)
            throws RemoteException {
        return this.engine.spawnUnit(this.player, name, at);
    }

    @Override
    public EngineEvent listen() throws RemoteException {
        synchronized (this.events) {
            if (this.events.isEmpty()) {
                try {
                    this.events.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return this.events.pop();
        }
    }

    @Override
    public void endTurn() throws RemoteException {
        this.engine.endTurn(this.player);
    }

    @Override
    public PlayerDTO getActivePlayer() throws RemoteException {
        return this.engine.getActivePlayer();
    }

    @Override
    public TileDTO popTile() {
        synchronized (this.tiles) {
            if (!this.tiles.isEmpty()) {
                return this.tiles.pop();
            } else {
                EngineInterface.log.error("The tiles stack is empty!");
                return null;
            }
        }
    }

    @Override
    public LinkedList<TileDTO> popStream() {
        synchronized (this.streams) {
            if (!this.streams.isEmpty()) {
                return this.streams.pop();
            } else {
                EngineInterface.log.error("The streams stack is empty!");
                return null;
            }
        }
    }

    @Deprecated
    @Override
    public PointDTO getStartPosition() throws RemoteException {
        PlayerDTO[] players = this.engine.getAllPlayers();
        int position = 0;
        for (int i = 0; i < players.length; i++) {
            if (players[i].getName().equals(this.player.getName())) {
                position = i;
                break;
            }
        }
        return new PointDTO(13, 8 - position * 2);
    }
}