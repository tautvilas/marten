package marten.physics.interactions;

import java.util.ArrayList;

import marten.physics.Feature;
import marten.physics.FeatureListener;
import marten.physics.Interaction;
import marten.physics.PhysicsObject;
import marten.util.Point;
import marten.util.Rotation;
import marten.util.Vector;

public class Gravity extends Interaction implements FeatureListener {
	public static final double CONSTANT = 6.674E-11;
	private double gravitationalParameter;
	private Point positionBackup;
	public Gravity(PhysicsObject owner, Iterable<PhysicsObject> newAffectedObjects) {
		super(owner, newAffectedObjects);
		this.owner.getFeature("MASS").addListener(this);
		this.owner.getFeature("POSITION").addListener(this);
		this.gravitationalParameter = owner.getFeature("MASS").getDouble("MASS") * Gravity.CONSTANT;
		this.positionBackup = owner.getFeature("POSITION").getPoint("POSITION");
	}
	@Override protected Iterable<String> affectedPrereq() {
		ArrayList<String> prereq = new ArrayList<String>();
		prereq.add("POSITION");
		return prereq;
	}
	@Override protected void interact(PhysicsObject target, double time) {
		Vector direction = new Vector (target.getFeature("POSITION").getPoint("POSITION"), this.positionBackup);
		target.getFeature("POSITION").setVector("ACCELERATION", direction.normalize().scale(this.gravitationalParameter / direction.lengthSquared()));
	}
	@Override protected Iterable<String> ownerPrereq() {
		ArrayList<String> prereq = new ArrayList<String>();
		prereq.add("POSITION");
		prereq.add("MASS");
		return prereq;
	}
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
	@Override public String getType() {
		return "GRAVITY";
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
	public void featureChange(Feature feature, String parameter) {
		if (parameter.equalsIgnoreCase("MASS"))
			this.gravitationalParameter = this.owner.getFeature("MASS").getDouble("MASS") * Gravity.CONSTANT;
		else if (parameter.equalsIgnoreCase("POSITION"))
			this.positionBackup = this.owner.getFeature("POSITION").getPoint("POSITION");
	}
}