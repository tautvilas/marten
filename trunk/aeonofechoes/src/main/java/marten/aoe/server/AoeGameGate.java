package marten.aoe.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import marten.aoe.server.face.ServerGameGate;

import org.apache.log4j.Logger;

public class AoeGameGate extends UnicastRemoteObject implements ServerGameGate {

    private static org.apache.log4j.Logger log = Logger
            .getLogger(ServerGameGate.class);

    private ClientSession creator;
    private ArrayList<ClientSession> players = new ArrayList<ClientSession>();
    private String gameName;
    @SuppressWarnings("unused")
    private String mapName;
    private boolean open = true;

    protected AoeGameGate(ClientSession creator, String gameName, String mapName)
            throws RemoteException {
        super();
        this.creator = creator;
        this.gameName = gameName;
        this.mapName = mapName;
        players.add(creator);
        log.info("Game gate for game '" + this.gameName + "' created");
    }

    private static final long serialVersionUID = 1L;

    @Override
    public void join(ClientSession session) throws RemoteException {
        if (open) {
            this.players.add(session);
            log.info("Client '" + session.username + "' joined game '"
                    + gameName + "' gate");
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
