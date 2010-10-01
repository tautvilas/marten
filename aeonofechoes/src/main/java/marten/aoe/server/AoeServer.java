package marten.aoe.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class AoeServer extends UnicastRemoteObject implements Server {
    private static final long serialVersionUID = 1L;
    private HashMap<String, ClientSession> users = new HashMap<String, ClientSession>();
    private HashMap<String, ClientMessageProvider> messengers = new HashMap<String, ClientMessageProvider>();

    private static org.apache.log4j.Logger log = Logger
            .getLogger(AoeServer.class);

    public static void start() {
        try {
            InetAddress publicIp = InetAddress.getLocalHost();
            if (Pattern.compile("127\\..*").matcher(publicIp.getHostAddress())
                    .matches()) {
                log.warn("System has a misconfigured public IP."
                        + " Trying to guess the right ip...");
                Socket socket = new Socket();
                socket.setReuseAddress(true);
                socket.connect(new InetSocketAddress("www.example.com", 80));
                publicIp = socket.getLocalAddress();
                socket.close();
            }

            System.setProperty("java.rmi.server.hostname", publicIp
                    .getHostAddress());
            log.debug("Binding server for IP " + publicIp.getHostAddress());
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            log.info("RMI registry started on port 1099");
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
    public void login(ClientSession session) throws RemoteException {
        String username = session.username;
        if (users.keySet().contains(username)) {
            log.error("Username '" + username + "' allready exists");
        } else {
            users.put(username, session);
            messengers.put(username, new ClientMessageProvider());
            log.info("User '" + username + "' successfully logged in");
        }
    }

    @Override
    public void sendMessage(ClientSession from, String to, String message)
            throws RemoteException {
        if (!users.keySet().contains(to)) {
            log.error("User '" + to + "' does not exist");
            return;
        }
        messengers.get(to).addChatMessage(
                new ChatMessage(from.username, message));
    }

    @Override
    public ChatMessage getMessage(ClientSession session) throws RemoteException {
        return messengers.get(session.username).getMessage();
    }
}
