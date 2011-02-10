package marten.aoe.server.face;

import java.rmi.Remote;
import java.rmi.RemoteException;

import marten.aoe.dto.MapDTO;
import marten.aoe.dto.PointDTO;

public interface EngineFace extends Remote {
    public MapDTO getMap() throws RemoteException;
    @Deprecated
    public boolean moveUnit(PointDTO from, PointDTO to) throws RemoteException;
    @Deprecated
    public boolean createUnit(String name, PointDTO at) throws RemoteException;
}
