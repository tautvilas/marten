package marten.aoe.server.face;

import java.rmi.Remote;
import java.rmi.RemoteException;

import marten.aoe.server.ClientSession;

public interface ServerGameGate extends Remote {
    public String start(ClientSession session) throws RemoteException;

    public void join(ClientSession session) throws RemoteException;

    public String[] getMembers(ClientSession session) throws RemoteException;
}
