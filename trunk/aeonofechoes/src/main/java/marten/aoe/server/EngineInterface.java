package marten.aoe.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import marten.aoe.proposal.dto.MinimalMapDTO;
import marten.aoe.proposal.engine.Engine;
import marten.aoe.proposal.engine.Player;
import marten.aoe.server.face.EngineFace;

public class EngineInterface extends UnicastRemoteObject implements EngineFace {

    private final Engine engine;
    @SuppressWarnings("unused")
    private final String username;

    protected EngineInterface(Engine engine, String username)
            throws RemoteException {
        super();
        this.engine = engine;
        this.username = username;
    }

    private static final long serialVersionUID = 1L;

    @Override
    public MinimalMapDTO getMap() throws RemoteException {
        // FIXME: patched to prevent compilation errors, MUST be changed appropriately.
        return engine.getMinimalMapDTO(Player.SYSTEM);
    }

}
