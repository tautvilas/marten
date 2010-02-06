package marten.age.io;

public interface LoadingState {
    public void setStatus(String status);

    public void setPercentage(int percentage);

    public void setEstimatable(boolean estimatable);
}
