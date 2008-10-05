package marten.age;

import marten.util.Rotation;
import marten.util.Vector;

import org.lwjgl.opengl.GL11;



public class CameraRotation implements CameraTransformation {
	private Rotation rotation = new Rotation();
	public CameraRotation (Rotation newRotation) {
		this.setRotation (newRotation);
	}
	public void setRotation (Rotation newRotation) {
		this.rotation = newRotation;
	}
	public Rotation getRotation () {
		return this.rotation;
	}
	public void transform() {
		double angle = this.rotation.getAngle() * 180.0 / Math.PI;
		Vector axis = this.rotation.getAxis();
		GL11.glRotatef (-(float)angle, (float)axis.x, (float)axis.y, (float)axis.z);
	}
}
