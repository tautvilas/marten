package marten.aoe.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;

import marten.aoe.server.face.ServerGame;
import marten.aoe.server.serializable.ClientSession;
import marten.aoe.server.serializable.GameNotification;

import org.apache.log4j.Logger;

public class AoeGame extends UnicastRemoteObject implements ServerGame {

    private static org.apache.log4j.Logger log = Logger
            .getLogger(ServerGame.class);
    private static final long serialVersionUID = 1L;

    private String creator;
    private HashMap<String, LinkedList<GameNotification>> players = new HashMap<String, LinkedList<GameNotification>>();
    private String gameName;
    private String mapName;
    private boolean open = true;

    @Override
    public synchronized String[] getMembers(ClientSession session)
            throws RemoteException {
        String[] m = new String[players.size()];
        return players.keySet().toArray(m);
    }

    @Override
    public GameNotification listen(ClientSession session)
            throws RemoteException {
        String username = Sessions.getUsername(session);
        LinkedList<GameNotification> notifier = players.get(username);
        synchronized (notifier) {
            if (notifier.isEmpty()) {
                try {
                    notifier.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return notifier.pop();
    }

    protected AoeGame(ClientSession creator, String gameName, String mapName)
            throws RemoteException {
        super();
        String username = Sessions.getUsername(creator);
        this.creator = username;
        this.gameName = gameName;
        this.mapName = mapName;
        players.put(username, new LinkedList<GameNotification>());
        log.info("Game gate for game '" + this.gameName + "' created");
    }

    @Override
    public void join(ClientSession session) throws RemoteException {
        String username = Sessions.getUsername(session);
        if (open) {
            players.put(username, new LinkedList<GameNotification>());
            publishNotification(GameNotification.PLAYER_LIST_UPDATED);
        } else {
            log.info(username + " tried to join but game was closed");
        }
    }

    @Override
    public void leave(ClientSession session) throws RemoteException {
        String username = Sessions.getUsername(session);
        if (players.containsKey(username)) {
            players.remove(username);
            if (username.equals(this.creator)) {
                publishNotification(GameNotification.CREATOR_QUIT);
            } else {
                publishNotification(GameNotification.PLAYER_LIST_UPDATED);
            }
        }
    }

    private void publishNotification(GameNotification notification) {
        for (LinkedList<GameNotification> notifier : players.values()) {
            synchronized (notifier) {
                notifier.add(notification);
                notifier.notifyAll();
            }
        }
    }

    @Override
    public void start(ClientSession session) throws RemoteException {
        String username = Sessions.getUsername(session);
        if (this.creator.equals(username)) {
            publishNotification(GameNotification.GAME_STARTED);
            this.open = false;
        }
        log.warn("Not authorized to start a game");
    }

    @Override
    public String getMapName(ClientSession session) {
        return this.mapName;
    }

}
