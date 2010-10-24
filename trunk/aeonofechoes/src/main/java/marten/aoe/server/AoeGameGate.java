package marten.aoe.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Set;

import marten.aoe.server.face.ServerGameGate;

import org.apache.log4j.Logger;

public class AoeGameGate extends UnicastRemoteObject implements ServerGameGate {

    private static org.apache.log4j.Logger log = Logger
            .getLogger(ServerGameGate.class);
    private static final long serialVersionUID = 1L;

    private ClientSession creator;
    private HashMap<String, ClientSession> players = new HashMap<String, ClientSession>();
    private volatile HashMap<String, Messenger> messengers = new HashMap<String, Messenger>();
    private String gameName;
    @SuppressWarnings("unused")
    private String mapName;
    private boolean open = true;

    private class Messenger {
        private boolean firstCall = true;

        public synchronized Set<String> getMembers() {
            log.info(firstCall);
            try {
                if (!firstCall) {
                    this.wait();
                } else {
                    firstCall = false;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return players.keySet();
        }

        public synchronized void resume() {
            // Seems that notify can not be called from other class...
            this.notify();
        }
    }

    @Override
    public synchronized String[] getMembers(ClientSession session) throws RemoteException {
        Set<String> members = messengers.get(session.username).getMembers();
        String[] m = new String[members.size()];
        return members.toArray(m);
    }

    protected AoeGameGate(ClientSession creator, String gameName, String mapName)
            throws RemoteException {
        super();
        this.creator = creator;
        this.gameName = gameName;
        this.mapName = mapName;
        players.put(creator.username, creator);
        messengers.put(creator.username, new Messenger());
        log.info("Game gate for game '" + this.gameName + "' created");
    }

    @Override
    public void join(ClientSession session) throws RemoteException {
        if (open) {
            players.put(session.username, session);
            messengers.put(session.username, new Messenger());
            for (Messenger messenger : messengers.values()) {
                messenger.resume();
            }
        } else {
            log.info(session.username + " tried to join but game was closed");
        }
    }

    @Override
    public String start(ClientSession session) throws RemoteException {
        if (this.creator == session) {
            return "xxx";
        }
        log.warn("Not authorized");
        return "";
    }

}
