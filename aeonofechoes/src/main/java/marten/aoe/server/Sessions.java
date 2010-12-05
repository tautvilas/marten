package marten.aoe.server;

import java.rmi.RemoteException;
import java.util.HashMap;

import marten.aoe.server.serializable.ClientSession;

import org.apache.log4j.Logger;

public class Sessions {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(Sessions.class);

    private static HashMap<String, ServerClient> container = new HashMap<String, ServerClient>();

    public static ServerClient getClient(String username) {
        return container.get(username);
    }

    public static ServerClient authenticate(ClientSession session) throws RemoteException {
        if (container.containsKey(session.id)) {
            ServerClient client = container.get(session.id);
            if (client.getSecret().equals(session.secret)) {
                return client;
            } else {
                String exception = "Incorrect secret for user '" + session.id + "'";
                log.error(exception);
                throw new RemoteException(exception);
            }
        } else {
            String exception = "User '" + session.id + "' has no session";
            log.error(exception);
            throw new RemoteException(exception);
        }
    }

    public static void removeClient(String username) {
        if (container.containsKey(username)) {
            container.remove(username);
        }
    }

    public static ClientSession addClient(String username) throws RemoteException {
        if (container.containsKey(username)) {
            String exception = "User '" + username + "' allready exists";
            log.error(exception);
            throw new RemoteException(exception);
        } else {
            ServerClient client = new ServerClient(username);
            container.put(username, client);
            return new ClientSession(username, client.getSecret());
        }
    }
}
