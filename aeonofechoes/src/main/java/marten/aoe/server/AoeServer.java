package marten.aoe.server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class AoeServer extends UnicastRemoteObject implements Server {
    private static final long serialVersionUID = 1L;
    private HashMap<String, Session> users = new HashMap<String, Session>();

    private static org.apache.log4j.Logger log = Logger
            .getLogger(AoeServer.class);

    public static void start() {
        try {
            Naming.rebind("rmi://localhost/Server", new AoeServer());
            log.info("AOE server is started!");
        } catch (Exception e) {
            log.error("Failed to start AOE server");
            throw new RuntimeException(e);
        }
    }

    public AoeServer() throws RemoteException {
        super();
    }

    @Override
    public void login(Session session) throws RemoteException {
        String username = session.getUsername();
        if (users.keySet().contains(username)) {
            log.error("Username '" + session.getUsername() + "' allready exists");
        } else {
            users.put(username, session);
            log.info("User '" + username + "' successfully logged in");
        }
    }

    @Override
    public void sendMessage(Session from, String to, String message)
            throws RemoteException {
        if(!users.keySet().contains(to)) {
            log.error("User '" + to + "' does not exist");
            return;
        }
        users.get(to).publishMessage(from.getUsername(), message);
    }
}