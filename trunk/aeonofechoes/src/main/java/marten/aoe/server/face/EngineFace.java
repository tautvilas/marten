package marten.aoe.server.face;

import java.rmi.Remote;
import java.rmi.RemoteException;

import marten.aoe.proposal.dto.MinimalMapDTO;

public interface EngineFace extends Remote {
    public MinimalMapDTO getMap() throws RemoteException;
}
