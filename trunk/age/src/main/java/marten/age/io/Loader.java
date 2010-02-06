package marten.age.io;

public interface Loader extends Runnable{
    public void load();

    public String getStatus();

    public int getPercentage();

    public boolean estimatable();

    public boolean loadingFinished();
}
