package marten.aoe.server.face;

import java.rmi.Remote;
import java.rmi.RemoteException;

import marten.aoe.dto.MapDTO;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.server.serializable.EngineEvent;

public interface EngineFace extends Remote {
    public MapDTO getMap() throws RemoteException;

    @Deprecated
    public void moveUnit(PointDTO from, PointDTO to) throws RemoteException;

    @Deprecated
    public boolean createUnit(String name, PointDTO at) throws RemoteException;

    public EngineEvent listen() throws RemoteException;

    public void endTurn() throws RemoteException;

    public PlayerDTO getActivePlayer() throws RemoteException;

    public TileDTO popTile() throws RemoteException;

    @Deprecated
    public PointDTO getStartPosition() throws RemoteException;
}
