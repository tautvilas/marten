package marten.age.control;

public interface KeyboardListener extends Listener {
    public void keyDown(int key, char character);

    public void keyUp(int key, char character);
}
