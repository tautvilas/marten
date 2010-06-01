package marten.age.control;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

public class KeyboardController extends Controller {

    public KeyboardController() {
        try {
            Keyboard.create();
        } catch (LWJGLException e) {
            throw new RuntimeException(e);
        }
        Keyboard.enableRepeatEvents(true);
    }

    public KeyboardController(KeyboardListener l) {
        super.addListener(l);
    }

    public void addListener(KeyboardListener l) {
        super.addListener(l);
    }

    @Override
    protected void publishEventsToListener(Listener l) {
        KeyboardListener listener = (KeyboardListener) l;
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                listener.keyDown(Keyboard.getEventKey(), Keyboard
                        .getEventCharacter());
            } else {
                listener.keyUp(Keyboard.getEventKey(), Keyboard
                        .getEventCharacter());
            }
        }
    }

    @Override
    protected void harvestEvents() {
    }

    @Override
    protected void listenerTypeCheck(Listener l) {
        if (!(l instanceof KeyboardListener)) {
            throw new ClassCastException("Incorrect listener type");
        }
    }
}
