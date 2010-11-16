package marten.aoe.server.face;

import java.rmi.Remote;
import java.rmi.RemoteException;

import marten.aoe.server.ClientSession;
import marten.aoe.server.GameNotification;

public interface ServerGameGate extends Remote {
    public void start(ClientSession session) throws RemoteException;

    public void join(ClientSession session) throws RemoteException;

    public String[] getMembers(ClientSession session) throws RemoteException;

    public GameNotification listen(ClientSession session) throws RemoteException;

    public String getMapName(ClientSession session) throws RemoteException;
}
