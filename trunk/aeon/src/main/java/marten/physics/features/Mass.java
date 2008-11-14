package marten.physics.features;

import marten.physics.Feature;
import marten.physics.PhysicsObject;
import marten.util.Point;
import marten.util.Rotation;
import marten.util.Vector;

public class Mass extends Feature {
	protected double mass;
	public Mass (PhysicsObject owner, double newMass) {
		super(owner);
		if (newMass < 0.0)
			throw new RuntimeException ("Attempted to set negative mass for the object.");
		this.mass = newMass;
	}
	@Override public void execute(double time) {}
	@Override public Object get(String param) {
		if (param.equalsIgnoreCase("MASS"))
			return new Double(mass);
		throw new RuntimeException ("Unsupported parameter " + param + " requested.");
	}
	@Override public double getDouble (String param) {
		if (param.equalsIgnoreCase("MASS"))
			return this.mass;
		throw new RuntimeException ("Unsupported parameter " + param + " requested.");
	}
	@Override public Point getPoint (String param) {
		throw new RuntimeException ("Unsupported parameter " + param + " requested.");
	}
	@Override public Rotation getRotation (String param) {
		throw new RuntimeException ("Unsupported parameter " + param + " requested.");
	}
	@Override public String getType() {
		return "MASS";
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
}