package marten.aoe.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import marten.aoe.server.face.ServerGameGate;

import org.apache.log4j.Logger;

public class AoeGameGate extends UnicastRemoteObject implements ServerGameGate {

    private static org.apache.log4j.Logger log = Logger
            .getLogger(ServerGameGate.class);
    private static final long serialVersionUID = 1L;

    private Object listListenerRegister = new Object();
    private String creator;
    private ArrayList<String> players = new ArrayList<String>();
    private String gameName;
    @SuppressWarnings("unused")
    private String mapName;
    private boolean open = true;

    @Override
    public synchronized String[] getMembers(ClientSession session) {
        String[] m = new String[players.size()];
        return players.toArray(m);
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
        String username = Sessions.getUsername(creator);
        this.creator = username;
        this.gameName = gameName;
        this.mapName = mapName;
        players.add(username);
        log.info("Game gate for game '" + this.gameName + "' created");
    }

    @Override
    public void join(ClientSession session) throws RemoteException {
        String username = Sessions.getUsername(session);
        if (open) {
            synchronized (listListenerRegister) {
                listListenerRegister.notifyAll();
            }
            players.add(username);
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
