package marten.aoe.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import marten.aoe.dto.FullTileDTO;
import marten.aoe.dto.MapDTO;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.engine.Engine;
import marten.aoe.engine.EngineListener;
import marten.aoe.engine.GlobalEvent;
import marten.aoe.engine.LocalEvent;
import marten.aoe.server.face.EngineFace;

import org.apache.log4j.Logger;

public class EngineInterface extends UnicastRemoteObject implements EngineFace {
    private static org.apache.log4j.Logger log = Logger
    .getLogger(EngineFace.class);

    private final Engine engine;
    private final PlayerDTO player;

    protected EngineInterface(Engine engine, PlayerDTO player)
    throws RemoteException {
        super();
        this.engine = engine;
        this.player = player;
    }

    private static final long serialVersionUID = 1L;

    @Override
    public MapDTO getMap() throws RemoteException {
        return this.engine.getMinimalMapDTO(this.player);
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
    public void addListener() throws RemoteException {
        this.engine.addListener(new EngineListener() {

            @Override
            public void onLocalEvent(LocalEvent event, FullTileDTO location) {
                EngineInterface.log.info(EngineInterface.this.player.getName() + event + ", " + location);
            }

            @Override
            public void onGlobalEvent(GlobalEvent event) {
                EngineInterface.log.info(EngineInterface.this.player.getName() + event);
            }

            @Override
            public PlayerDTO getAssignedPlayer() {
                return EngineInterface.this.player;
            }
        });
    }

    @Override
    public void endTurn() throws RemoteException {
        this.engine.endTurn(this.player);
    }

    @Override
    public PlayerDTO getActivePlayer() throws RemoteException {
        return this.engine.getActivePlayer();
    }

}
