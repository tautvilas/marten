package marten.aoe.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

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
    private LinkedList<EngineEvent> events = new LinkedList<EngineEvent>();
    private LinkedList<TileDTO> tiles = new LinkedList<TileDTO>();
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
                    synchronized (tiles) {
                        tiles.add(location);
                    }
                    synchronized (events) {
                        events.add(EngineEvent.TILE_UPDATE);
                        events.notifyAll();
//                        if (event == LocalEvent.UNIT_ENTRY || event == LocalEvent.UNIT_EXIT) {
//                            log.info(event + " " + location.getName() + " " + location.getCoordinates());
//                        }
                    }
                } else {
                    EngineInterface.log.info(EngineInterface.this.player
                            .getName()
                            + " " + event);
                }
            }

            @Override
            public void onGlobalEvent(GlobalEvent event) {
                EngineInterface.log.info(EngineInterface.this.player.getName()
                        + " " + event);
            }
        }, this.player);
    }

    @Override
    public MapDTO getMap() throws RemoteException {
        return this.engine.getMapDTO(this.player);
    }

    @Deprecated
    @Override
    public synchronized boolean moveUnit(PointDTO from, PointDTO to)
            throws RemoteException {
        return this.engine.moveUnit(this.player, from, to);
    }

    @Deprecated
    @Override
    public synchronized boolean createUnit(String name, PointDTO at)
            throws RemoteException {
        return this.engine.createUnit(this.player, name, at);
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
        synchronized (tiles) {
            if (!tiles.isEmpty()) {
                return this.tiles.pop();
            } else {
                log.error("The tiles stack is empty!");
                return null;
            }
        }
    }
}
