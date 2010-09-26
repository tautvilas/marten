package marten.aoe.server;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class AoeServer extends UnicastRemoteObject implements Server {
    private static final long serialVersionUID = 1L;
    private HashMap<String, Session> users = new HashMap<String, Session>();

    private static org.apache.log4j.Logger log = Logger
            .getLogger(AoeServer.class);

    private static boolean ipIsValid(InetAddress ip) {
        String ipString = ip.toString();
        if (ipString.equals("127.0.0.1") || ipString.equals("127.0.1.1")) {
            return false;
        }
        return true;
    }

    public static void start() {
        try {
            InetAddress publicIp = InetAddress.getLocalHost();
            if (!ipIsValid(publicIp)) {
                Enumeration<NetworkInterface> interfaces = NetworkInterface
                        .getNetworkInterfaces();
                log.debug("System has a misconfigured public IP."
                        + " Trying to guess the right ip...");
                while (interfaces.hasMoreElements()) {
                    NetworkInterface netInterface = interfaces.nextElement();
                    log.debug("Found net interface '" + netInterface.getName()
                            + "'");
                    Enumeration<InetAddress> addresses = netInterface
                            .getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();
                        log.debug("This interface has address "
                                + address.getHostAddress());
                        if (ipIsValid(address)) {
                            publicIp = address;
                        }
                    }
                }
            }
            log.info("Binding server for IP " + publicIp.getHostAddress());
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
            log.error("Username '" + session.getUsername()
                    + "' allready exists");
        } else {
            users.put(username, session);
            log.info("User '" + username + "' successfully logged in");
        }
    }

    @Override
    public void sendMessage(Session from, String to, String message)
            throws RemoteException {
        if (!users.keySet().contains(to)) {
            log.error("User '" + to + "' does not exist");
            return;
        }
        users.get(to).publishMessage(from.getUsername(), message);
    }
}
