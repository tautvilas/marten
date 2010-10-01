package marten.aoe.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientSession extends UnicastRemoteObject implements Session {

    private static final long serialVersionUID = 1L;

    private String username;

    public ClientSession(String username) throws RemoteException {
        super();
        this.username = username;
    }

    @Override
    public String getUsername() throws RemoteException {
        return this.username;
    }
}
