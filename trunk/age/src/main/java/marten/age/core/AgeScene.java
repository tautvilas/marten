package marten.age.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import marten.age.control.Controller;
import marten.age.control.Listener;
import marten.age.event.AgeEvent;
import marten.age.event.AgeEventListener;

import org.apache.log4j.Logger;

public abstract class AgeScene {
    private static org.apache.log4j.Logger log = Logger.getLogger(AgeScene.class);

    private HashSet<Controller> controllers = new HashSet<Controller>();
    private HashSet<AgeEventListener> listeners = new HashSet<AgeEventListener>();

    private LinkedList<Listener> removedListeners = new LinkedList<Listener>();
    private LinkedList<Listener> addedListeners = new LinkedList<Listener>();

    private HashMap<String, Listener> lookup = new HashMap<String, Listener>(); 

    /* Game "business" logic should go here */
    public abstract void compute();

    /* All the graphics related code goes here */
    public abstract void render();

    /* Init method unwanted stuff */
    public void init() {
    }

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
        this.addedListeners.add(listener);
    }

    public void updateControllable(String id, Listener listener) {
        if (this.lookup.containsKey(id)) {
            this.removedListeners.add(lookup.get(id));
        }
        this.addedListeners.add(listener);
        this.lookup.put(id, listener);
    }

    public void unbindControllable(Listener listener) {
        this.removedListeners.add(listener);
    }

    // HACK: use this method to avoid concurrent modification exception while updating
    // controllers
    protected void updateControllers() {
        while (!removedListeners.isEmpty()) {
            Listener listener = removedListeners.pop();
            for (Controller controller : this.controllers) {
                try {
                    controller.removeListener(listener);
                    log.debug(listener + " listener was removed from controller " + controller);
                } catch (ClassCastException e) {
                }
            }
        }
        while (!addedListeners.isEmpty()) {
            Listener listener = addedListeners.pop();
            for (Controller controller : this.controllers) {
                try {
                    controller.addListener(listener);
                    log.debug(listener + " listener was added to controller " + controller);
                } catch (ClassCastException e) {
                }
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
