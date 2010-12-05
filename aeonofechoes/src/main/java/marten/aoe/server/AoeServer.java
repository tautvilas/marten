package marten.aoe.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Pattern;

import marten.aoe.server.face.Server;
import marten.aoe.server.serializable.ChatMessage;
import marten.aoe.server.serializable.ClientSession;
import marten.aoe.server.serializable.GameDetails;
import marten.aoe.server.serializable.ServerNotification;

import org.apache.log4j.Logger;

public class AoeServer extends UnicastRemoteObject implements Server {
    private static final long serialVersionUID = 1L;
    private HashMap<String, ServerClient> clients = new HashMap<String, ServerClient>();
    private HashMap<String, GameDetails> games = new HashMap<String, GameDetails>();
    @SuppressWarnings("unused")
    private String serverUrl;

    private static org.apache.log4j.Logger log = Logger
            .getLogger(AoeServer.class);

    private static boolean serverStarted = false;

    public static void start() {
        if (serverStarted)
            return;
        try {
            InetAddress publicIp = InetAddress.getLocalHost();
            // HACK(zv): try to determine public IP by connecting to HTTP on
            // example.com
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
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            log.info("RMI registry started on port 1099");
            String serverUrl = "rmi://" + publicIp.getHostAddress() + "/Server";
            Naming.rebind("rmi://localhost/Server", new AoeServer(serverUrl));
            serverStarted = true;
            log.info("AOE server is started!");
        } catch (Exception e) {
            log.error("Failed to start AOE server");
            throw new RuntimeException(e);
        }
    }

    public AoeServer(String url) throws RemoteException {
        super();
        this.serverUrl = url;
    }

    @Override
    public ClientSession login(String username) throws RemoteException {
        ClientSession session = Sessions.addUser(username);
        clients.put(username, new ServerClient(username));
        log.info("User '" + username + "' with secret '" + session.secret
                + "' successfully logged in");
        return session;
    }

    @Override
    public ServerNotification listen(ClientSession session)
            throws RemoteException {
        String username = Sessions.getUsername(session);
        LinkedList<ServerNotification> notifier = clients.get(username)
                .getNotifier();
        synchronized (notifier) {
            if (notifier.isEmpty()) {
                try {
                    notifier.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return notifier.pop();
    }

    @Override
    public void leave(ClientSession session) throws RemoteException {
        String username = Sessions.getUsername(session);
        clients.remove(username);
        for (String game : games.keySet()) {
            GameDetails details = games.get(game);
            if (details.hasPlayer(username)) {
                this.leaveGame(session, game);
            }
        }
        Sessions.removeUser(username);
        log.info("User '" + username + "' logged out ");
    }

    @Override
    public void sendPrivateMessage(ClientSession from, String to, String message)
            throws RemoteException {
        String username = Sessions.getUsername(from);
        ServerClient client = clients.get(to);
        client.addMessage(new ChatMessage(username, message));
    }

    @Override
    public void sendGameMessage(ClientSession from, String gate, String message)
            throws RemoteException {
        String username = Sessions.getUsername(from);
        if (!games.containsKey(gate)) {
            throw new RemoteException("Game '" + gate
                    + "' does not exists");
        }
        GameDetails game = games.get(gate);
        for (String user : game.getPlayers()) {
            if (user != username) {
                clients.get(username).addMessage(new ChatMessage(username, message));
            }
        }
    }

    @Override
    public ChatMessage getMessage(ClientSession session) throws RemoteException {
        String username = Sessions.getUsername(session);
        return clients.get(username).getLastMessage();
    }

    @Override
    public synchronized void createGame(ClientSession session, String gameName,
            String mapName) throws RemoteException {
        String username = Sessions.getUsername(session);
        if (games.containsKey(gameName)) {
            log.error("Game '" + gameName + "' allready exists");
            throw new RemoteException("Game name allready exists");
        }
        GameDetails game = new GameDetails(username, mapName, gameName);
        this.games.put(gameName, game);
    }

    @Override
    public GameDetails getGameDetails(
            ClientSession session, String game) throws RemoteException {
//        String username = Sessions.getUsername(session);
        return games.get(game);
    }

    @Override
    public String[] getMembers(ClientSession session, String game)
            throws RemoteException {
//        String username = Sessions.getUsername(session);
        return games.get(game).getPlayers();
    }

    @Override
    public void joinGame(ClientSession session, String game)
            throws RemoteException {
        String username = Sessions.getUsername(session);
        GameDetails details = games.get(game);
        details.addPlayer(username);
        for (String member : details.getPlayers()) {
            clients.get(member).notify(ServerNotification.PLAYER_LIST_UPDATED);
        }
    }

    @Override
    public void leaveGame(ClientSession session, String game)
            throws RemoteException {
        String username = Sessions.getUsername(session);
        GameDetails details =  games.get(game);
        details.removePlayer(username);
        if (details.getNumPlayers() == 0) {
            games.remove(game);
        } else {
            for (String member : details.getPlayers()) {
                clients.get(member).notify(ServerNotification.PLAYER_LIST_UPDATED);
            }
        }
    }

    @Override
    public void startGame(ClientSession session, String game)
            throws RemoteException {
        String username = Sessions.getUsername(session);
        GameDetails details = games.get(game);
        if (details.getCreator().equals(username)) {
            for (String member : details.getPlayers()) {
                clients.get(member).notify(ServerNotification.GAME_STARTED);
            }
        } else {
            throw new RuntimeException("Unauthorized");
        }
    }
}
