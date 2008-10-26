package marten;

public abstract class Functionality {
	private PhysicsObject owner = null;
	public Functionality (PhysicsObject newOwner) {
		this.owner = newOwner;
	}
	public abstract void execute (double time);
	public abstract String getType ();
	public PhysicsObject getOwner () {
		return this.owner;
	}
}
