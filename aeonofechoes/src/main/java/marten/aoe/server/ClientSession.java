package marten.aoe.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientSession extends UnicastRemoteObject implements Session {

    private String username;

    public ClientSession(String username) throws RemoteException {
        super();
        this.username = username;
    }

    @Override
    public String getUsername() throws RemoteException {
        return this.username;
    }

    private static final long serialVersionUID = 1L;

    @Override
    public void publishMessage(String from, String message)
            throws RemoteException {
        // TODO Auto-generated method stub
        
    }

}
