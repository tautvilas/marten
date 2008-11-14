package marten.physics.features;

import marten.physics.PhysicsObject;
import marten.util.Point;
import marten.util.Vector;

public class ArbitrarilyChangingPosition extends ConstantlyChangingPosition {
	protected Vector accelerationAccumulator = new Vector();
	public ArbitrarilyChangingPosition (PhysicsObject owner, Point initialPosition, Vector initialVelocity) {
		super (owner, initialPosition, initialVelocity);
	}
	@Override public String getSubtype () {
		return "CHANGING VELOCITY";
	}
	@Override public void execute (double time) {
		if (this.lastActivation != Double.NEGATIVE_INFINITY) {
			this.velocity = this.velocity.add(this.accelerationAccumulator.scale(time - this.lastActivation));
			this.accelerationAccumulator = new Vector();
			this.notifyListeners("VELOCITY");
		}
		super.execute(time);
	}
	@Override public void set (String param, Object value) {
		if (param.equalsIgnoreCase("ACCELERATION"))
			this.accelerationAccumulator = this.accelerationAccumulator.add((Vector)value);
		else
			super.set(param, value);
	}
	@Override public void setVector (String param, Vector value) {
		if (param.equalsIgnoreCase("ACCELERATION"))
			this.accelerationAccumulator = this.accelerationAccumulator.add(value);
		else
			super.setVector(param, value);
	}
}
