package marten.aoe.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import marten.aoe.server.face.ServerGame;

public class AoeGame extends UnicastRemoteObject implements ServerGame{

    protected AoeGame() throws RemoteException {
        super();
    }

    private static final long serialVersionUID = 1L;

}
