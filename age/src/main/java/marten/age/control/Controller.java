package marten.age.control;

import java.util.HashSet;

public abstract class Controller {
	private HashSet<Listener> listeners = new HashSet<Listener>();
	
	public final void publishEvents() {
		harvestEvents();
		for(Listener l : listeners) {
			publishEventsToListener(l);
		}
	}
	
	protected void addListener(Listener l) {
		listeners.add(l);
	}
	
	public void removeListener(Listener l) {
		listeners.remove(l);
	}
	
	protected abstract void publishEventsToListener(Listener l);
	
	protected abstract void harvestEvents();
}
