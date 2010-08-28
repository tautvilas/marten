package marten.aoe.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientSession extends UnicastRemoteObject implements Session {

    protected ClientSession() throws RemoteException {
        super();
    }

    private static final long serialVersionUID = 1L;

}
