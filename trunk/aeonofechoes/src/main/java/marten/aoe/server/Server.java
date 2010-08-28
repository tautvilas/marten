package marten.aoe.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
    public Session login(String username) throws RemoteException;
}
