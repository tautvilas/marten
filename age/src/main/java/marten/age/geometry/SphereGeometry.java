package marten.age.geometry;

import marten.age.Geometry;

import org.lwjgl.opengl.glu.Sphere;


public class SphereGeometry implements Geometry {
	private double radius = 1.0;
	private int stacks = 32;
	private int slices = 64;
	
	private Sphere sphere = new Sphere();
	
	public SphereGeometry() {
	}
	
	public SphereGeometry(double radius) {
		this.radius = radius;
	}
	
	public SphereGeometry(double radius, int stacks, int splices) {
		this(radius);
		this.stacks = stacks;
		this.slices = splices;
	}
	
	public double getRadius() {
		return this.radius;
	}
	
	public void draw() {
		sphere.setTextureFlag(true);
		sphere.draw((float)radius, stacks, slices);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof SphereGeometry) {
			SphereGeometry g2 = (SphereGeometry)o;
			return this.radius == g2.getRadius();
		} else {
			return super.equals(o);
		}
	}
}
