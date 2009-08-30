package marten.age.transform;

import marten.age.BasicSceneGraphBranch;

import org.lwjgl.opengl.GL11;


public final class AssymetricScaleGroup extends BasicSceneGraphBranch {
	private double scaleFactorX = 1.0, scaleFactorY = 1.0, scaleFactorZ = 1.0;
	public AssymetricScaleGroup () {}
	public AssymetricScaleGroup (double newScaleFactorX, double newScaleFactorY, double newScaleFactorZ) {
		this.setScaleFactors (newScaleFactorX, newScaleFactorY, newScaleFactorZ);
	}
	public void setScaleFactors (double newScaleFactorX, double newScaleFactorY, double newScaleFactorZ) {
		this.scaleFactorX = newScaleFactorX;
		this.scaleFactorY = newScaleFactorY;
		this.scaleFactorZ = newScaleFactorZ;
	}
	public double[] getScaleFactors () {
		return new double[] {this.scaleFactorX, this.scaleFactorY, this.scaleFactorZ};
	}
	@Override public void render () {
		GL11.glPushMatrix();
		GL11.glScaled(this.scaleFactorX, this.scaleFactorY, this.scaleFactorZ);
		super.render();
		GL11.glPopMatrix();
	}
}
