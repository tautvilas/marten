package marten.physics;

import java.util.ArrayList;

public abstract class Interaction extends Feature{
	private ArrayList<PhysicsObject> affectedObjects = new ArrayList<PhysicsObject>();
	public Interaction (PhysicsObject owner, Iterable<PhysicsObject> newAffectedObjects) {
		super (owner);
		for (PhysicsObject object : newAffectedObjects)
			this.affectedObjects.add(object);
	}
	public final void addAffectedObject (PhysicsObject object) {
		this.affectedObjects.add(object);
	}
	public final void addAffectedObjects (Iterable<PhysicsObject> objects) {
		for (PhysicsObject object : objects)
			this.affectedObjects.add(object);
	}
	@Override public final void execute (double time) {
		for (PhysicsObject object : this.affectedObjects)
			this.interact (object, time);
	}
	protected abstract void interact (PhysicsObject target, double time);
}
