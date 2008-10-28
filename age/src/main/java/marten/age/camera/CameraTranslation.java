package marten.age.camera;

import marten.util.Point;

import org.lwjgl.opengl.GL11;



public class CameraTranslation implements CameraTransformation {
	// XXX:carnifex: remembering previous experiences with camera tweaking, shouldn't the points be negated before using?
	private Point coordinates = new Point();
	public CameraTranslation () {};
	public CameraTranslation (Point newCoordinates) {
		this.setCoordinates (newCoordinates);
	}
	public void setCoordinates (Point newCoordinates) {
		this.coordinates = new Point (newCoordinates);
	}
	public Point getCoordinates () {
		return new Point (this.coordinates);
	}	
	public void transform() {
		GL11.glTranslated(this.coordinates.x, this.coordinates.y, this.coordinates.z);
	}
}
