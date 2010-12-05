package marten.aoe.server;

import java.rmi.RemoteException;
import java.util.HashMap;

import marten.aoe.server.serializable.ClientSession;

import org.apache.log4j.Logger;

public class Sessions {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(Sessions.class);

    private static HashMap<String, ClientSession> container = new HashMap<String, ClientSession>();

    public static String getUsername(ClientSession session) throws RemoteException {
        if (container.containsKey(session.id)) {
            ClientSession serverSession = container.get(session.id);
            if (serverSession.secret.equals(session.secret)) {
                return session.id;
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

    public static void removeUser(String username) {
        if (container.containsKey(username)) {
            container.remove(username);
        }
    }

    public static ClientSession addUser(String username) throws RemoteException {
        if (container.containsKey(username)) {
            String exception = "User '" + username + "' allready exists";
            log.error(exception);
            throw new RemoteException(exception);
        } else {
            ClientSession session = new ClientSession(username);
            container.put(username, session);
            return session;
        }
    }
}
