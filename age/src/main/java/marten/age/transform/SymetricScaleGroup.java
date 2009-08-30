package marten.age.transform;

import marten.age.BasicSceneGraphBranch;

import org.lwjgl.opengl.GL11;


public final class SymetricScaleGroup extends BasicSceneGraphBranch {
	private double scaleFactor = 1.0;
	public SymetricScaleGroup () {}
	public SymetricScaleGroup (double newScaleFactor) {
		this.setScaleFactor (newScaleFactor);
	}
	public void setScaleFactor (double newScaleFactor) {
		this.scaleFactor = newScaleFactor;
	}
	public double getScaleFactor () {
		return this.scaleFactor;
	}
	@Override public void render () {
		GL11.glPushMatrix();
		GL11.glScaled(this.scaleFactor, this.scaleFactor, this.scaleFactor);
		super.render();
		GL11.glPopMatrix();
	}
}
