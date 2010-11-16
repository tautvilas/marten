package marten.aoe.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import marten.aoe.server.face.ServerGameGate;

import org.apache.log4j.Logger;

public class AoeGameGate extends UnicastRemoteObject implements ServerGameGate {

    private static org.apache.log4j.Logger log = Logger
            .getLogger(ServerGameGate.class);
    private static final long serialVersionUID = 1L;

    private String creator;
    private ArrayList<String> players = new ArrayList<String>();
    private HashMap<String, LinkedList<GameNotification>> notifiers = new HashMap<String, LinkedList<GameNotification>>();
    private String gameName;
    @SuppressWarnings("unused")
    private String mapName;
    private boolean open = true;

    @Override
    public synchronized String[] getMembers(ClientSession session)
            throws RemoteException {
        String[] m = new String[players.size()];
        return players.toArray(m);
    }

    @Override
    public GameNotification listen(ClientSession session)
            throws RemoteException {
        String username = Sessions.getUsername(session);
        LinkedList<GameNotification> notifier = notifiers.get(username);
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

    protected AoeGameGate(ClientSession creator, String gameName, String mapName)
            throws RemoteException {
        super();
        String username = Sessions.getUsername(creator);
        this.creator = username;
        this.gameName = gameName;
        this.mapName = mapName;
        players.add(username);
        notifiers.put(username, new LinkedList<GameNotification>());
        log.info("Game gate for game '" + this.gameName + "' created");
    }

    @Override
    public void join(ClientSession session) throws RemoteException {
        String username = Sessions.getUsername(session);
        if (open) {
            players.add(username);
            notifiers.put(username, new LinkedList<GameNotification>());
            for (LinkedList<GameNotification> notifier : notifiers.values()) {
                synchronized (notifier) {
                    notifier.add(GameNotification.PLAYER_LIST_UPDATED);
                    notifier.notifyAll();
                }
            }
        } else {
            log.info(username + " tried to join but game was closed");
        }
    }

    @Override
    public String start(ClientSession session) throws RemoteException {
        String username = Sessions.getUsername(session);
        if (this.creator == username) {
            return "xxx";
        }
        log.warn("Not authorized");
        return "";
    }

}
