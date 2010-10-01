package marten.age.core;

import java.util.HashSet;

import org.apache.log4j.Logger;

import marten.age.control.Controller;
import marten.age.control.Listener;
import marten.age.event.AgeEvent;
import marten.age.event.AgeEventListener;

public abstract class AgeScene {
    private static org.apache.log4j.Logger log = Logger.getLogger(AgeScene.class);

    private HashSet<Controller> controllers = new HashSet<Controller>();
    private HashSet<AgeEventListener> listeners = new HashSet<AgeEventListener>();

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

    protected HashSet<Controller> getControllers() {
        return this.controllers;
    }

    public void registerControllable(Listener listener) {
        for (Controller controller : this.controllers) {
            try {
                controller.addListener(listener);
                log.debug(listener + " listener was added to controller " + controller);
            } catch (ClassCastException e) {
            }
        }
    }

    public void addController(Controller c) {
        this.controllers.add(c);
    }

    public void removeController(Controller c) {
        this.controllers.remove(c);
    }
}
