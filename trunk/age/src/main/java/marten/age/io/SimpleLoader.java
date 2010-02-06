package marten.age.io;

public class SimpleLoader extends Thread implements Loader {
    private String status = "";
    private int percentage = 0;
    private boolean estimatable = false;
    private boolean loadingFinished = false;
    private Loadable loadable;

    public SimpleLoader(Loadable loadable) {
        this.loadable = loadable;
    }

    @Override
    public void load() {
        this.start();
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public int getPercentage() {
        return this.percentage;
    }

    @Override
    public boolean estimatable() {
        return this.estimatable;
    }

    @Override
    public boolean loadingFinished() {
        return this.loadingFinished;
    }

    @Override
    public void run() {
        loadable.load(this);
        this.loadingFinished = true;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public void setEstimatable(boolean estimatable) {
        this.estimatable = estimatable;
    }
}
