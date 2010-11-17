package marten.aoe.server;

import java.util.HashMap;

import marten.aoe.server.serializable.ClientSession;

import org.apache.log4j.Logger;

public class Sessions {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(Sessions.class);

    private static HashMap<String, ClientSession> container = new HashMap<String, ClientSession>();

    public static String getUsername(ClientSession session) {
        if (container.containsKey(session.id)) {
            ClientSession serverSession = container.get(session.id);
            if (serverSession.secret.equals(session.secret)) {
                return session.id;
            } else {
                log.error("Incorrect secret for user '" + session.id + "'");
                return null;
            }
        } else {
            log.error("User '" + session.id + "' has no session");
            return null;
        }
    }

    public static void removeUser(String username) {
        container.remove(username);
    }

    public static ClientSession addUser(String username) {
        if (container.containsKey(username)) {
            return null;
        } else {
            ClientSession session = new ClientSession(username);
            container.put(username, session);
            return session;
        }
    }
}
