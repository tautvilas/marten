package marten.aoe.server;

public abstract class ServerListener extends Thread {

    public ServerListener() {
        super();
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            this.listen();
        }
    }

    public abstract void listen();

}
