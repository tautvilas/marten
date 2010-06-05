package marten.aoe.server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.log4j.Logger;

public class AoeServer extends UnicastRemoteObject implements Server {
    private static final long serialVersionUID = 1L;

    private static org.apache.log4j.Logger log = Logger
            .getLogger(AoeServer.class);

    private String message;

    public static void start() {
        try {
            Naming.rebind("//localhost/Server", new AoeServer("Hello world"));
            log.info("AOE server is started!");
        } catch (Exception e) {
            log.error("Failed to start AOE server");
            throw new RuntimeException(e);
        }
    }

    public AoeServer(String message) throws RemoteException {
        this.message = message;
    }

    @Override
    public String say() throws RemoteException {
        return message;
    }

    
}
