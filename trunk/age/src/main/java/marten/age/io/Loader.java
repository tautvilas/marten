package marten.age.io;

public class Loader extends Thread {
    private String status = "";
    private int percentage = 0;
    private boolean estimatable = false;
    private boolean loadingFinished = false;
    private Loadable loadable;

    public Loader(Loadable loadable) {
        this.loadable = loadable;
    }

    public void load() {
        this.start();
    }

    public String getStatus() {
        return this.status;
    }

    public int getPercentage() {
        return this.percentage;
    }

    public boolean estimatable() {
        return this.estimatable;
    }

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
