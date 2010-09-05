package marten.aoe.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public abstract class ClientSession extends UnicastRemoteObject implements Session {

    private String username;

    public ClientSession(String username) throws RemoteException {
        super();
        this.username = username;
    }

    @Override
    public String getUsername() throws RemoteException {
        return this.username;
    }

    @Override
    public abstract void publishMessage(String from, String message) throws RemoteException;

    private static final long serialVersionUID = 1L;

}
