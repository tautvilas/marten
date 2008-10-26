package marten;

import java.util.HashMap;

public final class PhysicsObject {
	private HashMap<String, Functionality> functionalities = new HashMap<String, Functionality>();
	private HashMap<String, Interaction> interactions = new HashMap<String, Interaction>();
	private boolean lock = false;
	public PhysicsObject () {}
	public void addFunctionality (Functionality functionality) {
		if (this.lock)
			throw new RuntimeException ("Attempted to add functionality to a compiled object.");
		this.functionalities.put(functionality.getType(), functionality);
	}
	public void activate (double time) {
		if (!this.lock)
			throw new RuntimeException ("Attempted to activate an incomplete object.");
		for (Functionality functionality : this.functionalities.values())
			functionality.execute(time);
	}
	public void interact (double time) {
		if (!this.lock)
			throw new RuntimeException ("Attempted to cause an interaction with an incomplete object.");
		for (Interaction interaction : this.interactions.values())
			interaction.execute(time);
	}
	public void compile () {
		this.lock = true;
	}
}
