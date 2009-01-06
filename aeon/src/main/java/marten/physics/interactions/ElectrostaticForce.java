package marten.physics.interactions;

import marten.physics.Feature;
import marten.physics.FeatureListener;
import marten.physics.Interaction;
import marten.physics.PhysicsObject;
import marten.util.Point;
import marten.util.Rotation;
import marten.util.Vector;

public final class ElectrostaticForce extends Interaction implements FeatureListener {
	public static final double ELECTRIC_CONSTANT = 8.988E9;
	private double electricParameter;
	private Point positionBackup;
	public ElectrostaticForce (PhysicsObject owner, Iterable<PhysicsObject> affectedObjects) {
		super (owner, affectedObjects);
		this.getOwner().getFeature("CHARGE").addListener(this);
		this.getOwner().getFeature("POSITION").addListener(this);
		this.electricParameter = ElectrostaticForce.ELECTRIC_CONSTANT * owner.getFeature("CHARGE").getDouble("CHARGE");
		this.positionBackup = this.getOwner().getFeature("POSITION").getPoint("POSITION");
	}
	@Override public String getType() {
		return "ELECTROSTATIC";
	}
	@Override protected void interact(PhysicsObject target, double time) {
		Vector direction = new Vector (this.positionBackup, target.getFeature("POSITION").getPoint("POSITION"));
		target.getFeature("POSITION").setVector("FORCE", direction.normalize().scale(this.electricParameter * target.getFeature("CHARGE").getDouble("CHARGE") / direction.lengthSquared()));
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
	@Override public Vector getVector(String param) {
		throw new RuntimeException ("Unsupported parameter " + param + " requested.");
	}
	@Override public void set(String param, Object value) {
		throw new RuntimeException ("Unsupported parameter " + param + " requested.");
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
	public void featureChange (Feature feature, String parameter, double value) {
		if (parameter.equalsIgnoreCase("CHARGE"))
			this.electricParameter = ElectrostaticForce.ELECTRIC_CONSTANT * value;
	}
	public void featureChange(Feature feature, String parameter, Vector value) {}
	public void featureChange(Feature feature, String parameter, Point value) {
		if (parameter.equalsIgnoreCase("POSITION"))
			this.positionBackup = value;
	}
	public void featureChange(Feature feature, String parameter, Rotation value) {}
	public void featureChange(Feature feature, String parameter, Object value) {}
}
