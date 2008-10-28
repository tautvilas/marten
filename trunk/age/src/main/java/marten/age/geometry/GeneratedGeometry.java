package marten.age.geometry;


import org.lwjgl.opengl.GL11;


public abstract class GeneratedGeometry implements Geometry {
	private int list = 0;
	
	abstract public int generate ();
	public void draw() {
		if (list == 0)
			list = this.generate();
		GL11.glCallList(this.list);
	}
}
