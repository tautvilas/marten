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
    private HashMap<String, ServerGame> games = new HashMap<String, ServerGame>();
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
            Naming.rebind(serverUrl, new AoeServer(serverUrl));
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
        ClientSession session = Sessions.addClient(username);
        log.info("User '" + username + "' with secret '" + session.secret
                + "' successfully logged in");
        return session;
    }

    @Override
    public ServerNotification listen(ClientSession session)
            throws RemoteException {
        ServerClient client = Sessions.authenticate(session);
        LinkedList<ServerNotification> notifier = client.getNotifier();
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
        ServerClient client = Sessions.authenticate(session);
        for (String gameName : games.keySet()) {
            ServerGame game = games.get(gameName);
            if (game.hasPlayer(client)) {
                this.leaveGame(session, gameName);
            }
        }
        Sessions.removeClient(client.getUsername());
        log.info("User '" + client.getUsername() + "' logged out ");
    }

    @Override
    public void sendPrivateMessage(ClientSession from, String to, String message)
            throws RemoteException {
        ServerClient fromClient = Sessions.authenticate(from);
        ServerClient toClient = Sessions.getClient(to);
        toClient.addMessage(new ChatMessage(fromClient.getUsername(), message));
    }

    @Override
    public void sendGameMessage(ClientSession from, String gate, String message)
            throws RemoteException {
        ServerClient client = Sessions.authenticate(from);
        if (!games.containsKey(gate)) {
            throw new RemoteException("Game '" + gate
                    + "' does not exists");
        }
        ServerGame game = games.get(gate);
        for (ServerClient gameClient : game.getPlayers()) {
            if (!gameClient.getUsername().equals(client.getUsername())) {
                gameClient.addMessage(new ChatMessage(client.getUsername(), message));
            }
        }
    }

    @Override
    public ChatMessage getMessage(ClientSession session) throws RemoteException {
        return Sessions.authenticate(session).getLastMessage();
    }

    @Override
    public synchronized void createGame(ClientSession session, String gameName,
            String mapName) throws RemoteException {
        ServerClient user = Sessions.authenticate(session);
        if (games.containsKey(gameName)) {
            log.error("Game '" + gameName + "' allready exists");
            throw new RemoteException("Game name allready exists");
        }
        ServerGame game = new ServerGame(user, mapName, gameName, this.serverUrl);
        this.games.put(gameName, game);
    }

    @Override
    public GameDetails getGameDetails(
            ClientSession session, String game) throws RemoteException {
        ServerClient client = Sessions.authenticate(session);
        return games.get(game).getDetails(client);
    }

    @Override
    public void joinGame(ClientSession session, String gameName)
            throws RemoteException {
        ServerClient client = Sessions.authenticate(session);
        ServerGame game = games.get(gameName);
        game.addPlayer(client);
    }

    @Override
    public void leaveGame(ClientSession session, String gameName)
            throws RemoteException {
        ServerClient client = Sessions.authenticate(session);
        ServerGame game = games.get(gameName);
        game.removePlayer(client);
        if (game.getNumPlayers() == 0) {
            games.remove(game);
        }
    }

    @Override
    public void startGame(ClientSession session, String gameName)
            throws RemoteException {
        Sessions.authenticate(session);
        ServerGame game = games.get(gameName);
        game.start();
    }
}
