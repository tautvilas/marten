package marten.physics.interactions;

import marten.physics.Feature;
import marten.physics.FeatureListener;
import marten.physics.Interaction;
import marten.physics.PhysicsObject;
import marten.util.Point;
import marten.util.Rotation;
import marten.util.Vector;

public abstract class DistanceSquaredAttractor extends Interaction implements FeatureListener {
	private Point positionBackup;
	public DistanceSquaredAttractor (PhysicsObject newOwner, Iterable<PhysicsObject> newAffectedObjects) {
		super (newOwner, newAffectedObjects);
		this.getOwner().getFeature("POSITION").addListener(this);
		this.positionBackup = this.getOwner().getFeature("POSITION").getPoint("POSITION");
	}
	@Override protected final void interact(PhysicsObject target, double time) {
		Vector direction = new Vector (target.getFeature("POSITION").getPoint("POSITION"), this.positionBackup);
		target.getFeature("POSITION").setVector("ACCELERATION", direction.normalize().scale(this.attractiveForce () / direction.lengthSquared()));
	}
	protected abstract double attractiveForce ();
	@Override public Object get(String param) {
		throw new RuntimeException ("Unsupported parameter " + param + " requested.");
	}
	@Override public double getDouble(String param) {
		throw new RuntimeException ("Unsupported parameter " + param + " requested.");
	}
	@Override public Point getPoint(String param) {
		throw new RuntimeException ("Unsupported parameter " + param + " requested.");
	}
	@Override public Rotation getRotation(String param) {
		throw new RuntimeException ("Unsupported parameter " + param + " requested.");
	}
	@Override public Vector getVector(String param) {
		throw new RuntimeException ("Unsupported parameter " + param + " requested.");
	}
	@Override public void set(String param, Object value) {
		throw new RuntimeException ("Attempted to set unknown parameter " + param + ".");
	}
	@Override public void setDouble(String param, double value) {
		throw new RuntimeException ("Attempted to set unknown parameter " + param + ".");
	}
	@Override public void setPoint(String param, Point value) {
		throw new RuntimeException ("Attempted to set unknown parameter " + param + ".");
	}
	@Override public void setRotation(String param, Rotation value) {
		throw new RuntimeException ("Attempted to set unknown parameter " + param + ".");
	}
	@Override public void setVector(String param, Vector value) {
		throw new RuntimeException ("Attempted to set unknown parameter " + param + ".");
	}
	public void featureChange(Feature feature, String parameter, double value) {}
	public void featureChange(Feature feature, String parameter, Vector value) {}
	public void featureChange(Feature feature, String parameter, Point value) {
		if (parameter.equalsIgnoreCase("POSITION"))
			this.positionBackup = value;
		
	}
	public void featureChange(Feature feature, String parameter, Rotation value) {}
	public void featureChange(Feature feature, String parameter, Object value) {}
}
