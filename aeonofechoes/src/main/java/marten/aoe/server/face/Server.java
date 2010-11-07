package marten.aoe.server.face;

import java.rmi.Remote;
import java.rmi.RemoteException;

import marten.aoe.server.ChatMessage;
import marten.aoe.server.ClientSession;

public interface Server extends Remote {
    public void login(ClientSession session) throws RemoteException;

    public void sendPrivateMessage(ClientSession from, String to, String message)
            throws RemoteException;

    public void sendGateMessage(ClientSession from, String gate, String message)
            throws RemoteException;

    public ChatMessage getMessage(ClientSession session) throws RemoteException;

    public String createGame(ClientSession session, String gameName,
            String mapName) throws RemoteException;

    public String getGateUrl(String gateName) throws RemoteException;
}
