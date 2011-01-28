package marten.aoe.server.face;

import java.rmi.Remote;
import java.rmi.RemoteException;

import marten.aoe.dto.MapDTO;

public interface EngineFace extends Remote {
    public MapDTO getMap() throws RemoteException;
}
