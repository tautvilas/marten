package marten.aoe.server;

import java.rmi.RemoteException;

import marten.aoe.server.face.Server;
import marten.aoe.server.serializable.ClientSession;

public abstract class ServerListener extends NetworkListener {

    private Server server;
    private ClientSession session;
    private ServerPinger pinger;


    private class ServerPinger extends Thread {
        @Override
        public void run() {
            while (!isFinished()) {
                try {
                    server.ping(session);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ServerListener(Server server, ClientSession session) {
        super();
        this.server = server;
        this.session = session;
        this.pinger = new ServerPinger();
        this.pinger.start();
        this.start();
    }

    public abstract void listen();

}
