package marten.aoe.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Session extends Remote {
    public String getUsername() throws RemoteException;

    public ChatMessage popMessage() throws RemoteException;
}
