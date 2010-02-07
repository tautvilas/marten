package marten.age.io;

public class SimpleLoader implements Loader {
    private boolean loadingFinished = false;
    private LoadingState loadingState = new LoadingState();
    private Loadable loadable;
    private Thread loadThread;

    public SimpleLoader(Loadable loadable) {
        this.loadable = loadable;
    }

    @Override
    public void load() {
        loadThread = new Thread(this);
        loadThread.start();
    }

    @Override
    public String getStatus() {
        return loadingState.status;
    }

    @Override
    public int getPercentage() {
        return loadingState.percentage;
    }

    @Override
    public boolean loadingFinished() {
        return this.loadingFinished;
    }

    @Override
    public void run() {
        loadable.load(loadingState);
        this.loadingFinished = true;
    }
}
