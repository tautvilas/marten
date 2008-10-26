package marten;

import java.util.ArrayList;

public final class PhysicalSystem {
	private ArrayList<PhysicsObject> system = new ArrayList<PhysicsObject>();
	public void run (double time) {
		for (PhysicsObject object: this.system)
			object.interact(time);
		for (PhysicsObject object: this.system)
			object.activate(time);
	}
}
