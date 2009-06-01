package marten.age.geometry.primitives;

import marten.age.geometry.Geometry;

public class Sphere implements Geometry {
    private double radius = 1.0;
    private int stacks = 32;
    private int slices = 64;

    private org.lwjgl.opengl.glu.Sphere sphere =
            new org.lwjgl.opengl.glu.Sphere();

    public Sphere() {
    }

    public Sphere(double radius) {
        this.radius = radius;
    }

    public Sphere(double radius, int stacks, int splices) {
        this(radius);
        this.stacks = stacks;
        this.slices = splices;
    }

    public double getRadius() {
        return this.radius;
    }

    public void draw() {
        sphere.setTextureFlag(true);
        sphere.draw((float) radius, stacks, slices);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Sphere) {
            Sphere g2 = (Sphere) o;
            return this.radius == g2.getRadius();
        } else {
            return super.equals(o);
        }
    }
}
