package marten.aoe.server.face;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import marten.aoe.dto.MapDTO;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.server.serializable.EngineEvent;

public interface EngineFace extends Remote {
    public MapDTO getMap() throws RemoteException;

    public void performAction(PointDTO from, PointDTO to, int action)
            throws RemoteException;

    public EngineEvent listen() throws RemoteException;

    public void endTurn() throws RemoteException;

    public PlayerDTO getActivePlayer() throws RemoteException;

    public PlayerDTO getPlayer() throws RemoteException;

    public LinkedList<TileDTO> popStream() throws RemoteException;

    public TileDTO popTile() throws RemoteException;

    public void selectUnit(PointDTO location) throws RemoteException;

    public List<PointDTO> getPath(PointDTO destination) throws RemoteException;
}
