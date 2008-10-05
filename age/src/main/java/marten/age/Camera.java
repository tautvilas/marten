package marten.age;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import marten.util.Point;
import marten.util.Rotation;

abstract public class Camera {
	private ArrayList<CameraTransformation> settings = new ArrayList<CameraTransformation>();
	private double nearClippingPlane = 1.0;
	private double farClippingPlane = 10.0;
	private double nearWidth = 4.0;
	private double nearHeight = 3.0;
	public void setSettings (ArrayList<CameraTransformation> newSettings) {
		this.settings = newSettings;
	}
	public void setSettings (Point centeredAt, Rotation rotatedBy, double setAway) {
		this.setSettings(centeredAt, rotatedBy, new Point (0.0, 0.0, setAway));		
	}
	public void setSettings (Point centeredAt, Rotation rotatedBy, Point setAway) {
		this.settings = new ArrayList<CameraTransformation>();
		this.settings.add(new CameraTranslation(setAway));
		this.settings.add(new CameraRotation(rotatedBy));
		this.settings.add(new CameraTranslation(centeredAt));		
	}
	public ArrayList<CameraTransformation> getSettings () {
		return this.settings;
	}
	public void setClippingPlanes (double near, double far) {
		if (far <= near)
			throw new RuntimeException("Attempted to set far clipping plane closer or equal to near clipping plane");
		this.farClippingPlane = far;
		this.nearClippingPlane = near;
	}
	public double getNearClippingPlane () {
		return this.nearClippingPlane;
	}
	public double getFarClippingPlane () {
		return this.farClippingPlane;
	}
	public void setViewport (double width) {
		double height = width * Display.getDisplayMode().getHeight() / Display.getDisplayMode().getWidth();
		this.setViewport(width, height);
	}
	public void setViewport (double width, double height) {
		if (width <= 0.0)
			throw new RuntimeException ("Attempted to set a viewport with negative or zero width. Check if your Display is already initialized.");
		if (height <= 0.0)
			throw new RuntimeException ("Attempted to set a viewport with negative or zero height. Check if your Display is already initialized.");
		this.nearWidth = width;
		this.nearHeight = height;
	}
	public double getWidth () {
		return this.nearWidth;
	}
	public double getHeight () {
		return this.nearHeight;
	}
	abstract public void set ();
}
