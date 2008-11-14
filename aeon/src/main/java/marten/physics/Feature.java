package marten.physics;

import java.util.ArrayList;

import marten.util.Point;
import marten.util.Rotation;
import marten.util.Vector;

/** All physical features of objects must be implemented through this class.
 * @author Petras Razanskas*/
public abstract class Feature {
	/** The physical object to which this feature is attributed.*/
	private PhysicsObject owner = null;
	/** The objects that listen to the changes in the characteristics of this feature.*/
	private ArrayList<FeatureListener> listeners = new ArrayList<FeatureListener>();
	/** The default constructor of a feature that assigns an owner to it.
	 * @param newOwner The physical object to which this feature is assigned.*/
	public Feature (PhysicsObject newOwner) {
		this.owner = newOwner;
	}
	/** Subscribes an object to all updates about the state of this feature.
	 * @param listener The listening object.*/
	public final void addListener (FeatureListener listener) {
		this.listeners.add(listener);
	}
	/** The subclasses of <code>Feature</code> should use this method to inform all listeners about updated state of the feature.
	 * @param param The name of the updated parameter.
	 * @param value The new value of the updated parameter.*/
	protected final void notifyListeners (String param, double value) {
		for (FeatureListener listener : this.listeners)
			listener.featureChange(this, param, value);
	}
	/** The subclasses of <code>Feature</code> should use this method to inform all listeners about updated state of the feature.
	 * @param param The name of the updated parameter.
	 * @param value The new value of the updated parameter.*/
	protected final void notifyListeners (String param, Vector value) {
		for (FeatureListener listener : this.listeners)
			listener.featureChange(this, param, value);
	}
	/** The subclasses of <code>Feature</code> should use this method to inform all listeners about updated state of the feature.
	 * @param param The name of the updated parameter.
	 * @param value The new value of the updated parameter.*/
	protected final void notifyListeners (String param, Point value) {
		for (FeatureListener listener : this.listeners)
			listener.featureChange(this, param, value);
	}
	/** The subclasses of <code>Feature</code> should use this method to inform all listeners about updated state of the feature.
	 * @param param The name of the updated parameter.
	 * @param value The new value of the updated parameter.*/
	protected final void notifyListeners (String param, Rotation value) {
		for (FeatureListener listener : this.listeners)
			listener.featureChange(this, param, value);
	}
	/** The subclasses of <code>Feature</code> should use this method to inform all listeners about updated state of the feature.
	 * @param param The name of the updated parameter.
	 * @param value The new value of the updated parameter.*/
	protected final void notifyListeners (String param, Object value) {
		for (FeatureListener listener : this.listeners)
			listener.featureChange(this, param, value);
	}
	/** The subclasses should update their state inside this method.
	 * @param time The current time of the system. It may have nothing to do with the real time, it is the current time of the physics engine.*/
	public abstract void execute (double time);
	/** @return a name defining the type and purpose of this feature.*/
	public abstract String getType ();
	/** @return a <code>double</code> value of the given parameter.
	 * @param param a parameter the value of which is queried.
	 * @throws This method must cause a <code>RuntimeException</code> if a non-existant <code>double</code> parameter is queried.*/
	public abstract double getDouble (String param);
	/** @return a <code>Vector</code> value of the given parameter.
	 * @param param a parameter the value of which is queried.
	 * @throws This method must cause a <code>RuntimeException</code> if a non-existant <code>Vector</code> parameter is queried.*/
	public abstract Vector getVector (String param);
	/** @return a <code>Point</code> value of the given parameter.
	 * @param param a parameter the value of which is queried.
	 * @throws This method must cause a <code>RuntimeException</code> if a non-existant <code>Point</code> parameter is queried.*/
	public abstract Point getPoint (String param);
	/** @return a <code>Rotation</code> value of the given parameter.
	 * @param param a parameter the value of which is queried.
	 * @throws This method must cause a <code>RuntimeException</code> if a non-existant <code>Rotation</code> parameter is queried.*/
	public abstract Rotation getRotation (String param);
	/** This method is a fall back for exotic data types not inherently supported by Marten.
	 * However, if queried about existing data of <code>double</code>, <code>Vector</code>, <code>Point</code>, <code>Rotation</code> types, it should return those values anyway. 
	 * @return a <code>Object</code> value of the given parameter.
	 * @param param a parameter the value of which is queried.
	 * @throws This method must cause a <code>RuntimeException</code> if a non-existant <code>Object</code> parameter is queried.*/
	public abstract Object get (String param);
	/** Sets a <code>double</code> value of a certain parameter.
	 * @param param The name of the parameter being updated.
	 * @param value The new value of the updated parameter.
	 * @throws This method must cause a <code>RuntimeException</code> if a non-existant or read-only parameter is being updated. */
	public abstract void setDouble (String param, double value);
	/** Sets a <code>Vector</code> value of a certain parameter.
	 * @param param The name of the parameter being updated.
	 * @param value The new value of the updated parameter.
	 * @throws This method must cause a <code>RuntimeException</code> if a non-existant or read-only parameter is being updated. */
	public abstract void setVector (String param, Vector value);
	/** Sets a <code>Point</code> value of a certain parameter.
	 * @param param The name of the parameter being updated.
	 * @param value The new value of the updated parameter.
	 * @throws This method must cause a <code>RuntimeException</code> if a non-existant or read-only parameter is being updated. */
	public abstract void setPoint (String param, Point value);
	/** Sets a <code>Rotation</code> value of a certain parameter.
	 * @param param The name of the parameter being updated.
	 * @param value The new value of the updated parameter.
	 * @throws This method must cause a <code>RuntimeException</code> if a non-existant or read-only parameter is being updated. */
	public abstract void setRotation (String param, Rotation value);
	/** Sets a <code>Object</code> value of a certain parameter.
	 * This is a fallback method for data types not inherently supported by Marten.
	 * However, if a parameter is attempted to be updated through this method, it should work even if it is a <code>double</code>, <code>Vector</code>, <code>Point</code> or <code>Rotation</code> value.
	 * @param param The name of the parameter being updated.
	 * @param value The new value of the updated parameter.
	 * @throws This method must cause a <code>RuntimeException</code> if a non-existant or read-only parameter is being updated. */
	public abstract void set (String param, Object value);
	/**@return The physical object to which this feature is attributed to.*/
	public final PhysicsObject getOwner () {
		return this.owner;
	}
}
