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
import java.util.LinkedList;
import java.util.regex.Pattern;

import marten.aoe.server.face.Server;

import org.apache.log4j.Logger;

public class AoeServer extends UnicastRemoteObject implements Server {
    private static final long serialVersionUID = 1L;
    private HashMap<String, ClientSession> sessions;;
    private HashMap<String, LinkedList<ChatMessage>> inboxes;
    private HashMap<String, AoeGameGate> gameGates;
    private String serverUrl;

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
            String serverUrl = "rmi://" + publicIp.getHostAddress() + "/Server";
            Naming.rebind("rmi://localhost/Server", new AoeServer(serverUrl));
            log.info("AOE server is started!");
        } catch (Exception e) {
            log.error("Failed to start AOE server");
            throw new RuntimeException(e);
        }
    }

    public AoeServer(String url) throws RemoteException {
        super();
        this.serverUrl = url;
        this.inboxes = new HashMap<String, LinkedList<ChatMessage>>();
        this.sessions = new HashMap<String, ClientSession>();
        this.gameGates = new HashMap<String, AoeGameGate>();
    }

    @Override
    public void login(ClientSession session) throws RemoteException {
        String username = session.username;
        if (sessions.keySet().contains(username)) {
            log.error("Username '" + username + "' allready exists");
        } else {
            sessions.put(username, session);
            inboxes.put(username, new LinkedList<ChatMessage>());
            log.info("User '" + username + "' successfully logged in");
        }
    }

    @Override
    public void sendPrivateMessage(ClientSession from, String to, String message)
            throws RemoteException {
        if (!inboxes.containsKey(to)) {
            log.error("Username '" + to + "' does not exists");
            return;
        }
        LinkedList<ChatMessage> msgs = inboxes.get(to);
        msgs.add(new ChatMessage(from.username, message));
        synchronized (msgs) {
            msgs.notifyAll();
        }
    }

    @Override
    public void sendGateMessage(ClientSession from, String gate, String message) {
        if (!gameGates.containsKey(gate)) {
            log.error("Game gate '" + gate + "' does not exists");
            return;
        }
        AoeGameGate gameGate = gameGates.get(gate);
        for (String user : gameGate.getMembers(from)) {
            if (user != from.username) {
                LinkedList<ChatMessage> msgs = inboxes.get(user);
                synchronized (msgs) {
                    msgs.add(new ChatMessage(from.username, message));
                    msgs.notifyAll();
                }
            }
        }
    }

    @Override
    public ChatMessage getMessage(ClientSession session) throws RemoteException {
        LinkedList<ChatMessage> msgs = inboxes.get(session.username);
        synchronized (msgs) {
            if (msgs.isEmpty()) {
                try {
                    msgs.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return msgs.pop();
        }
    }

    @Override
    public String createGame(ClientSession session, String gameName,
            String mapName) throws RemoteException {
        AoeGameGate gate = new AoeGameGate(session, gameName, mapName);
        this.gameGates.put(gameName, gate);
        try {
            Naming.bind("rmi://localhost/Server/gates/" + gameName, gate);
        } catch (MalformedURLException e) {
            log.warn("Can not bind game gate: address is malformed");
        } catch (AlreadyBoundException e) {
            log.warn("Can not bind game: address allready in use");
        }
        return "/gates/" + gameName;
    }

    @Override
    public String getGateUrl(String gateName) throws RemoteException {
        return this.serverUrl + "/gates/" + gateName;
    }
}
