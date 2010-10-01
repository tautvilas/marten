package marten.aoe.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
    public void login(ClientSession session) throws RemoteException;

    public void sendMessage(ClientSession from, String to, String message)
            throws RemoteException;
}
