package marten.age.transform;

import marten.age.SceneGraphBranch;
import marten.util.Point;

import org.lwjgl.opengl.GL11;


public final class TranslationGroup extends SceneGraphBranch {
	private Point coordinates = new Point();
	public TranslationGroup () {};
	public TranslationGroup (Point newCoordinates) {
		this.setCoordinates (newCoordinates);
	}
	public void setCoordinates (Point newCoordinates) {
		this.coordinates = new Point (newCoordinates);
	}
	public Point getCoordinates () {
		return new Point (this.coordinates);
	}
	@Override public void activate () {
		GL11.glPushMatrix();
		GL11.glTranslated(this.coordinates.x, this.coordinates.y, this.coordinates.z);
		super.activate();
		GL11.glPopMatrix();
	}
}
