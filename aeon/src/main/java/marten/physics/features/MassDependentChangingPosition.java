package marten.physics.features;

import marten.physics.Feature;
import marten.physics.FeatureListener;
import marten.physics.PhysicsObject;
import marten.util.Point;
import marten.util.Vector;

public class MassDependentChangingPosition extends ArbitrarilyChangingPosition implements FeatureListener {
	private double massBackup;
	public MassDependentChangingPosition (PhysicsObject newOwner, Point initialPosition, Vector initialVelocity) {
		super (newOwner, initialPosition, initialVelocity);
		if (!this.owner.hasFeature("MASS"))
			throw new RuntimeException ("Required feature \'MASS\' not found in the owning object.");
		this.owner.getFeature("MASS").addListener(this);
		this.massBackup = this.owner.getFeature("MASS").getDouble("MASS");
	}
	public void featureChange(Feature feature, String parameter) {
		if (parameter.equalsIgnoreCase("MASS"))
			this.massBackup = this.owner.getFeature("MASS").getDouble("MASS");
	}
	@Override public void set (String param, Object value) {
		if (param.equalsIgnoreCase("FORCE"))
			this.accelerationAccumulator = this.accelerationAccumulator.add(((Vector)value).scale(this.massBackup));
		else
			super.set(param, value);
	}
	@Override public void setVector (String param, Vector value) {
		if (param.equalsIgnoreCase("FORCE"))
			this.accelerationAccumulator = this.accelerationAccumulator.add(value.scale(this.massBackup));
		else
			super.setVector(param, value);
	}
}
