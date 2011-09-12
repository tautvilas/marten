package marten.age.control;

import java.util.HashSet;

import marten.age.graphics.SceneGraphChild;

public abstract class Controller {
    private HashSet<Listener> listeners = new HashSet<Listener>();

    public final void publishEvents() {
        harvestEvents();
        for (Listener l : listeners) {
            if (l instanceof SceneGraphChild) {
                if (((SceneGraphChild)l).isHidden()) {
                    continue;
                }
            }
            publishEventsToListener(l);
        }
    }

    public void addListener(Listener l) {
        listenerTypeCheck(l);
        listeners.add(l);
    }

    public void removeListener(Listener l) {
        listenerTypeCheck(l);
        listeners.remove(l);
    }

    public int getNumListeners() {
        return this.listeners.size();
    }

    protected abstract void listenerTypeCheck(Listener l);

    protected abstract void publishEventsToListener(Listener l);

    protected abstract void harvestEvents();
}
