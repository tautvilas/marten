package marten.age.graphics.camera;


import org.lwjgl.opengl.GL11;

public final class OrthoCamera extends Camera {
	@Override
	public void set() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(-this.getWidth()/2.0, this.getWidth()/2.0, -this.getHeight()/2.0, this.getHeight() / 2.0, this.getNearClippingPlane(), this.getFarClippingPlane());
		for (CameraTransformation setting : this.getSettings())
			setting.transform();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
}
