package marten.physics;

import java.util.ArrayList;

import marten.util.Point;
import marten.util.Rotation;
import marten.util.Vector;

public abstract class Feature {
	protected PhysicsObject owner = null;
	private ArrayList<FeatureListener> listeners = new ArrayList<FeatureListener>();
	public Feature (PhysicsObject newOwner) {
		this.owner = newOwner;
	}
	public void addListener (FeatureListener listener) {
		this.listeners.add(listener);
	}
	protected void notifyListeners (String param) {
		for (FeatureListener listener : this.listeners)
			listener.featureChange(this, param);
	}
	public abstract void execute (double time);
	public abstract String getType ();
	public abstract String getSubtype ();
	public abstract double getDouble (String param);
	public abstract Vector getVector (String param);
	public abstract Point getPoint (String param);
	public abstract Rotation getRotation (String param);
	public abstract Object get (String param);
	public abstract void setDouble (String param, double value);
	public abstract void setVector (String param, Vector value);
	public abstract void setPoint (String param, Point value);
	public abstract void setRotation (String param, Rotation value);
	public abstract void set (String param, Object value);
	public PhysicsObject getOwner () {
		return this.owner;
	}
}
