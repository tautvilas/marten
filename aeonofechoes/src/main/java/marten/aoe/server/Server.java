package marten.aoe.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
    public String say() throws RemoteException;
}
