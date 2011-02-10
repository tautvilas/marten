package marten.aoe.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import marten.aoe.dto.MapDTO;
import marten.aoe.dto.PlayerDTO;
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

}
