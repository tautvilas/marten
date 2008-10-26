package marten;

import java.util.ArrayList;

public abstract class Interaction extends Functionality{
	private ArrayList<PhysicsObject> affectedObjects = new ArrayList<PhysicsObject>();
	public Interaction (PhysicsObject owner, Iterable<PhysicsObject> newAffectedObjects) {
		super (owner);
		for (PhysicsObject object : newAffectedObjects)
			this.affectedObjects.add(object);
	}
	@Override public void execute (double time) {
		for (PhysicsObject object : this.affectedObjects)
			this.interact (object, time);
	}
	public abstract void interact (PhysicsObject target, double time); 
}
