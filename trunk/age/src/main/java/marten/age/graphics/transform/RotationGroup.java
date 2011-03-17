package marten.age.graphics.transform;

import marten.age.graphics.BasicSceneGraphBranch;
import marten.age.graphics.SceneGraphChild;
import marten.age.graphics.primitives.Rotation;
import marten.age.graphics.primitives.Vector;

import org.lwjgl.opengl.GL11;


public final class RotationGroup extends BasicSceneGraphBranch<SceneGraphChild> {
	private double angle;
	private Vector axis = new Vector();
	public RotationGroup () {}
	public RotationGroup (Rotation newRotation) {
		this.setRotation (newRotation);
	}
	public void setRotation (Rotation newRotation) {
		this.angle = newRotation.getAngle() * 180.0 / Math.PI;
		this.axis = newRotation.getAxis();
	}
	public Rotation getRotation () {
		return new Rotation (axis, angle * Math.PI / 180.0);
	}
	@Override public void render () {
		GL11.glPushMatrix ();
		GL11.glRotatef ((float)angle, (float)axis.x, (float)axis.y, (float)axis.z);
		super.render();
		GL11.glPopMatrix();
	}
}
