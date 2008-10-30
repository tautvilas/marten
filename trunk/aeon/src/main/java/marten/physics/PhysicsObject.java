package marten.physics;

import java.util.HashMap;

public final class PhysicsObject {
	private HashMap<String, Feature> features = new HashMap<String, Feature>();
	private HashMap<String, Interaction> interactions = new HashMap<String, Interaction>();
	private boolean lock = false;
	public PhysicsObject () {}
	public Feature getFeature (String name) {
		if (this.features.containsKey(name))
			return this.features.get(name);
		throw new RuntimeException ("Attempted to get an unexisting feature.");
	}
	public void addFunctionality (Feature functionality) {
		if (this.lock)
			throw new RuntimeException ("Attempted to add a feature to a compiled object.");
		this.features.put(functionality.getType(), functionality);
	}
	public void activate (double time) {
		if (!this.lock)
			throw new RuntimeException ("Attempted to activate an incomplete object.");
		for (Feature functionality : this.features.values())
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
	public boolean prereq (Iterable<String> prereqList) {
		for (String featurePrereq : prereqList)
			if (!this.features.containsKey(featurePrereq))
				return false;
		return true;
	}
}
