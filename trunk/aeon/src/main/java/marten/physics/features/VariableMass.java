package marten.physics.features;

import marten.physics.PhysicsObject;

public class VariableMass extends Mass {
	protected double deltaMass = 0.0;
	public VariableMass(PhysicsObject owner, double newMass) {
		super(owner, newMass);
	}
	@Override public void set (String param, Object value) {
		if (param.equalsIgnoreCase("DELTAMASS"))			
			this.deltaMass = ((Double)value).doubleValue();
		else
			throw new RuntimeException ("Attempted to set unknown parameter " + param + ".");
	}
	@Override public void setDouble (String param, double value) {
		if (param.equalsIgnoreCase("DELTAMASS"))
			this.deltaMass = value;
		else
			throw new RuntimeException ("Attempted to set unknown parameter " + param + ".");
	}
	@Override public void execute (double time) {
		if (this.deltaMass + this.mass < 0.0)
			throw new RuntimeException ("Attempted to remove more mass than is present.");
		this.mass += this.deltaMass;
		this.deltaMass = 0.0;
		this.notifyListeners("MASS");
	}
}
