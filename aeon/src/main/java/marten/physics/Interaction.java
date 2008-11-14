package marten.physics;

import java.util.ArrayList;

public abstract class Interaction extends Feature{
	private ArrayList<PhysicsObject> affectedObjects = new ArrayList<PhysicsObject>();
	private boolean integrityChecked = false;
	public Interaction (PhysicsObject owner, Iterable<PhysicsObject> newAffectedObjects) {
		super (owner);
		if (!this.owner.prereq(this.ownerPrereq()))
			throw new RuntimeException ("Owner failed to fulfil interaction requirements.");
		for (PhysicsObject object : newAffectedObjects)
			this.affectedObjects.add(object);
	}
	public void addAffectedObject (PhysicsObject object) {
		this.affectedObjects.add(object);
		this.integrityChecked = false;
	}
	public void addAffectedObjects (Iterable<PhysicsObject> objects) {
		for (PhysicsObject object : objects)
			this.affectedObjects.add(object);
		this.integrityChecked = false;
	}
	@Override public void execute (double time) {
		if (!this.integrityChecked) {
			for (PhysicsObject object : this.affectedObjects)
				if (!object.prereq(this.affectedPrereq()))
					throw new RuntimeException ("Affected object to fulfil interaction requirements.");
			this.integrityChecked = true;
		}			
		for (PhysicsObject object : this.affectedObjects)
			this.interact (object, time);
	}
	protected abstract void interact (PhysicsObject target, double time);
	protected abstract Iterable<String> ownerPrereq();
	protected abstract Iterable<String> affectedPrereq();
}
