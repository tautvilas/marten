package marten.aoe.server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;

import org.apache.log4j.Logger;

public class AoeServer extends UnicastRemoteObject implements Server {
    private static final long serialVersionUID = 1L;
    private HashSet<String> users = new HashSet<String>();

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
    public Session login(String username) throws RemoteException {
        if (users.contains(username)) {
            log.error("Username '" + username + "' allready exists");
            return null;
        } else {
            users.add(username);
            log.info("User '" + "' successfully logged in");
            return new ClientSession();
        }
    }
}
