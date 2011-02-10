package marten.aoe.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import marten.aoe.dto.MapDTO;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.engine.Engine;
import marten.aoe.server.face.EngineFace;

public class EngineInterface extends UnicastRemoteObject implements EngineFace {

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
        return engine.getMinimalMapDTO(this.player);
    }

    @Deprecated
    @Override
    public synchronized boolean moveUnit(PointDTO from, PointDTO to) {
        return this.engine.moveUnit(this.player, from, to);
    }

    @Deprecated
    @Override
    public synchronized boolean createUnit(String name, PointDTO at) {
        return this.engine.createUnit(this.player, name, at);
    }

}
