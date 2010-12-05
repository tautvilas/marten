package marten.aoe.server.face;

import java.rmi.Remote;
import java.rmi.RemoteException;

import marten.aoe.server.serializable.ChatMessage;
import marten.aoe.server.serializable.ClientSession;
import marten.aoe.server.serializable.GameDetails;
import marten.aoe.server.serializable.ServerNotification;

public interface Server extends Remote {
    public ClientSession login(String username) throws RemoteException;

    public void leave(ClientSession session) throws RemoteException;

    public ServerNotification listen(ClientSession session)
            throws RemoteException;

    /** Messaging **/

    public void sendPrivateMessage(ClientSession from, String to, String message)
            throws RemoteException;

    public void sendGameMessage(ClientSession from, String game, String message)
            throws RemoteException;

    public ChatMessage getMessage(ClientSession session) throws RemoteException;

    /** Game methods **/

    public void createGame(ClientSession session, String gameName,
            String mapName) throws RemoteException;

    public void startGame(ClientSession session, String game)
            throws RemoteException;

    public void joinGame(ClientSession session, String game)
            throws RemoteException;

    public void leaveGame(ClientSession session, String game)
            throws RemoteException;

    public String[] getMembers(ClientSession session, String game)
            throws RemoteException;

    public GameDetails getGameDetails(ClientSession session, String game)
            throws RemoteException;
}
