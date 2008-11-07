package marten.physics.features;

import marten.physics.PhysicsObject;

public class ChangeableMass extends Mass {
	public ChangeableMass(PhysicsObject owner, double newMass) {
		super(owner, newMass);
	}
	@Override public String getSubtype () {
		return "CHANGEABLE";
	}
	@Override public void set (String param, Object value) {
		if (param.equalsIgnoreCase("MASS")) {
			if (!(value instanceof Double))
				throw new RuntimeException ("Attempted to set mass with an object of incompatible type.");
			if (((Double)value).doubleValue() < 0)
				throw new RuntimeException ("Attempted to set negative mass.");
			this.mass = ((Double)value).doubleValue();
			this.notifyListeners("MASS");
		}
		else
			throw new RuntimeException ("Attempted to set unknown parameter " + param + ".");
	}
	@Override public void setDouble (String param, double value) {
		if (param.equalsIgnoreCase("MASS")) {
			if ((this.mass = value) < 0)
				throw new RuntimeException ("Attempted to set negative mass.");
			this.notifyListeners("MASS");
		}
		else
			throw new RuntimeException ("Attempted to set unknown parameter " + param + ".");
	}
}
