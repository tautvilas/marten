package marten.aoe.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.regex.Pattern;

import marten.aoe.server.face.Server;
import marten.aoe.server.face.ServerGameGate;

import org.apache.log4j.Logger;

public class AoeServer extends UnicastRemoteObject implements Server {
    private static final long serialVersionUID = 1L;
    private HashMap<String, ClientSession> users = new HashMap<String, ClientSession>();
    private HashMap<String, ClientMessageProvider> messengers = new HashMap<String, ClientMessageProvider>();
    public static String serverUrl;

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
            serverUrl = "rmi://" + publicIp.getHostAddress() + "/Server";
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

    @Override
    public String createGame(ClientSession session, String gameName,
            String mapName) throws RemoteException {
        ServerGameGate gate = new AoeGameGate(session, gameName, mapName);
        try {
            Naming.bind("rmi://localhost/Server/gates/" + gameName, gate);
        } catch (MalformedURLException e) {
            log.warn("Can not bind game gate: address is malformed");
        } catch (AlreadyBoundException e) {
            log.warn("Can not bind game: address allready in use");
        }
        return "/gates/" + gameName;
    }

    @SuppressWarnings("static-access")
    @Override
    public String getGateUrl(String gateName) {
        return this.serverUrl + "/gates/" + gateName;
    }
}
