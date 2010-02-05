package marten.age.core;

import java.util.HashSet;

import marten.age.event.AgeEvent;
import marten.age.event.AgeEventListener;

public abstract class AgeScene {

    private HashSet<AgeEventListener> listeners = new HashSet<AgeEventListener>();

    /* Method for initializing the app */
    public abstract void init();

    /* Game "business" logic should go here */
    public abstract void compute();

    /* All the graphics related code goes here */
    public abstract void render();

    /* Method for destroying unwanted stuff */
    public void cleanup() {
    }

    /* Method for publishing events */
    public void fireEvent(AgeEvent e) {
        for (AgeEventListener l : listeners) {
            l.handle(e);
        }
    }

    /* Method for listener registration */
    public void registerListener(AgeEventListener listener) {
        this.listeners.add(listener);
    }
}
