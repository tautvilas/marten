package marten.physics.interactions;

import marten.physics.Feature;
import marten.physics.FeatureListener;
import marten.physics.PhysicsObject;

public class Gravity extends DistanceSquaredAttractor implements FeatureListener {
	public static final double GRAVITY_CONSTANT = 6.674E-11;
	private double gravitationalParameter;
	public Gravity(PhysicsObject owner, Iterable<PhysicsObject> newAffectedObjects) {
		super(owner, newAffectedObjects);
		this.getOwner().getFeature("MASS").addListener(this);
		this.gravitationalParameter = owner.getFeature("MASS").getDouble("MASS") * Gravity.GRAVITY_CONSTANT;
	}
	public void featureChange(Feature feature, String parameter, double value) {
		if (parameter.equalsIgnoreCase("MASS"))
			this.gravitationalParameter = value * Gravity.GRAVITY_CONSTANT;
		else
			super.featureChange(feature, parameter, value);
	}
	@Override protected double attractiveForce() {
		return this.gravitationalParameter;
	}
	@Override public String getType() {
		return "GRAVITY";
	}
}