package marten.physics.features;

import marten.physics.Feature;
import marten.physics.PhysicsObject;
import marten.util.Point;
import marten.util.Rotation;
import marten.util.Vector;

public final class ElectricCharge extends Feature {
	protected double charge;
	public ElectricCharge (PhysicsObject owner, double newCharge) {
		super (owner);
		this.charge = newCharge;
	}
	@Override public void execute(double time) {
	}
	@Override public Object get(String param) {
		if (param.equalsIgnoreCase("CHARGE"))
			return new Double(this.charge);
		throw new RuntimeException ("Unsupported parameter " + param + " requested.");
	}
	@Override public double getDouble(String param) {
		if (param.equalsIgnoreCase("CHARGE"))
			return this.charge;
		throw new RuntimeException ("Unsupported parameter " + param + " requested.");
	}
	@Override public Point getPoint(String param) {
		throw new RuntimeException ("Unsupported parameter " + param + " requested.");
	}
	@Override public Rotation getRotation(String param) {
		throw new RuntimeException ("Unsupported parameter " + param + " requested.");
	}
	@Override public String getType() {
		return "CHARGE";
	}
	@Override public Vector getVector(String param) {
		throw new RuntimeException ("Unsupported parameter " + param + " requested.");
	}
	@Override public void set(String param, Object value) {
		throw new RuntimeException ("Attempted to set unknown or read-only parameter " + param + ".");
	}
	@Override public void setDouble(String param, double value) {
		throw new RuntimeException ("Attempted to set unknown or read-only parameter " + param + ".");
	}
	@Override public void setPoint(String param, Point value) {
		throw new RuntimeException ("Attempted to set unknown or read-only parameter " + param + ".");
	}
	@Override public void setRotation(String param, Rotation value) {
		throw new RuntimeException ("Attempted to set unknown or read-only parameter " + param + ".");
	}
	@Override public void setVector(String param, Vector value) {
		throw new RuntimeException ("Attempted to set unknown or read-only parameter " + param + ".");
	}
}
