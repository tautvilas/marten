package marten.physics.features;

import marten.physics.Feature;
import marten.physics.PhysicsObject;
import marten.util.Point;
import marten.util.Rotation;
import marten.util.Vector;

public class Position extends Feature {
	protected Point position;
	public Position (PhysicsObject owner, Point newPosition) {
		super (owner);
		this.position = newPosition;
	}
	@Override public void execute(double time) {}
	@Override public Object get(String param) {
		if (param.equalsIgnoreCase("POSITION"))
			return this.position;
		if (param.equalsIgnoreCase("VELOCITY"))
			return new Vector ();
		throw new RuntimeException ("Unsupported parameter " + param + " requested.");
	}
	@Override public double getDouble(String param) {
		throw new RuntimeException ("Unsupported parameter " + param + " requested.");
	}
	@Override public Point getPoint(String param) {
		if (param.equalsIgnoreCase("POSITION"))
			return this.position;
		throw new RuntimeException ("Unsupported parameter " + param + " requested.");
	}
	@Override public Rotation getRotation(String param) {
		throw new RuntimeException ("Unsupported parameter " + param + " requested.");
	}
	@Override public String getType() {
		return "POSITION";
	}
	@Override public Vector getVector(String param) {
		if (param.equalsIgnoreCase("VELOCITY"))
			return new Vector();
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
