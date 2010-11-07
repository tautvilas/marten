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

    private Object listListenerRegister = new Object();
    private ClientSession creator;
    private HashMap<String, ClientSession> players = new HashMap<String, ClientSession>();
    private String gameName;
    @SuppressWarnings("unused")
    private String mapName;
    private boolean open = true;

    @Override
    public synchronized String[] getMembers(ClientSession session) {
        Set<String> members = this.players.keySet();
        String[] m = new String[members.size()];
        return members.toArray(m);
    }

    @Override
    public void listen() throws RemoteException {
        synchronized (listListenerRegister) {
            try {
                listListenerRegister.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected AoeGameGate(ClientSession creator, String gameName, String mapName)
            throws RemoteException {
        super();
        this.creator = creator;
        this.gameName = gameName;
        this.mapName = mapName;
        players.put(creator.username, creator);
        log.info("Game gate for game '" + this.gameName + "' created");
    }

    @Override
    public void join(ClientSession session) throws RemoteException {
        if (open) {
            synchronized (listListenerRegister) {
                listListenerRegister.notifyAll();
            }
            players.put(session.username, session);
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
