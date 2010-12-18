package marten.aoe.server;

import java.rmi.RemoteException;

import marten.aoe.server.face.Server;
import marten.aoe.server.serializable.ClientSession;

public abstract class ServerListener extends Thread {

    private Server server;
    private ClientSession session;
    private ServerPinger pinger;
    private boolean finished = false;

    public void quit() {
        this.finished = true;
    }

    private class ServerPinger extends Thread {
        @Override
        public void run() {
            while (!finished) {
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

    @Override
    public void run() {
        while (!finished) {
            this.listen();
        }
    }

    public abstract void listen();

}
