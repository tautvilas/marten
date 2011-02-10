package marten.aoe.server;

public abstract class NetworkListener extends Thread {

    private boolean finished = false;

    public void quit() {
        this.finished = true;
    }

    @Override
    public void run() {
        while (!finished) {
            this.listen();
        }
    }

    public boolean isFinished() {
        return this.finished;
    }

    public abstract void listen();
}
