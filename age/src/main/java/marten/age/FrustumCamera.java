package marten.age;

import org.lwjgl.opengl.GL11;

public final class FrustumCamera extends Camera {
	@Override
	public void set() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glFrustum(-this.getWidth()/2.0, this.getWidth()/2.0, -this.getHeight()/2.0, this.getHeight() / 2.0, this.getNearClippingPlane(), this.getFarClippingPlane());
		for (CameraTransformation setting : this.getSettings())
			setting.transform();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	public void setClippingPlanes (double near, double far) {
		if (far <= 0.0)
			throw new RuntimeException("Attempted to set negative or zero far clipping plane for frustum camera.");
		if (near <= 0.0)
			throw new RuntimeException("Attempted to set negative or zero near clipping plane for frustum camera.");
		super.setClippingPlanes(near, far);
	}
	public void setViewportByFOV (double fov) {
		if (fov <= 0.0)
			throw new RuntimeException ("Attempted to set negative or zero field of view angle.");
		if (fov >= Math.PI)
			throw new RuntimeException ("Attempted to set field of view angle, larger than PI radians.");
		double width = this.getNearClippingPlane() / Math.tan(fov / 2.0);
		this.setViewport(width);
	}
}
