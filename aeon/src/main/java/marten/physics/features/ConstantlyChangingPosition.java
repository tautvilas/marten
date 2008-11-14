package marten.physics.features;

import marten.physics.PhysicsObject;
import marten.util.Point;
import marten.util.Vector;

public class ConstantlyChangingPosition extends Position {
	protected Vector velocity;
	protected double lastActivation = Double.NEGATIVE_INFINITY;
	public ConstantlyChangingPosition (PhysicsObject owner, Point initialPosition, Vector newVelocity) {
		super (owner, initialPosition);
		this.velocity = newVelocity;
	}
	@Override public void execute (double time) {
		if (this.lastActivation != Double.NEGATIVE_INFINITY)
			this.position = this.position.move(this.velocity.scale(time - this.lastActivation));
		this.lastActivation = time;
	}
	@Override public Object get (String param) {
		if (param.equalsIgnoreCase("VELOCITY"))
			return this.velocity;
		return super.get(param);
	}
	@Override public Vector getVector (String param) {
		if (param.equalsIgnoreCase("VELOCITY"))
			return this.velocity;
		return super.getVector(param);
	}
}
