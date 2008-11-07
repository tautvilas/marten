package marten.physics.features;

import marten.physics.Feature;
import marten.physics.FeatureListener;
import marten.physics.PhysicsObject;

public class MassDependentRadius extends Radius implements FeatureListener {
	protected double density = 0.0;
	public MassDependentRadius (PhysicsObject newOwner, double newDensity) {
		super (newOwner, 0.0);
		if (!this.owner.hasFeature("MASS"))
			throw new RuntimeException ("Required feature \'MASS\' not found in the owning object.");
		this.owner.getFeature("MASS").addListener(this);
		if (newDensity < 0.0)
			throw new RuntimeException ("Attempted to set negative density.");
		this.density = newDensity;
		this.radius = Math.pow(0.75 * this.owner.getFeature("MASS").getDouble("MASS") / (this.density * Math.PI), 1.0/3.0); 
	}
	public void featureChange(Feature feature, String parameter) {
		if (parameter.equalsIgnoreCase("MASS")) {
			this.radius = Math.pow(0.75 * this.owner.getFeature("MASS").getDouble("MASS") / (this.density * Math.PI), 1.0/3.0);
			this.notifyListeners("RADIUS");
		}
	}
	@Override public double getDouble (String param) {
		if (param.equalsIgnoreCase("DENSITY"))
			return this.density;
		return super.getDouble(param);
	}
	@Override public Object get (String param) {
		if (param.equalsIgnoreCase("DENSITY"))
			return new Double(this.density);
		return super.get(param);
	}
}
