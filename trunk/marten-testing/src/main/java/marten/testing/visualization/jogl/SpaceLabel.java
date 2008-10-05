package marten.testing.visualization.jogl;

import javax.media.opengl.GL;

import marten.util.Point;



import com.sun.opengl.util.GLUT;

public class SpaceLabel {
	private String text;
	private Point location;

	public SpaceLabel(String text, Point location) {
		this.text = text;
		this.location = location;
	}

	public void display(GL gl) {
		GLUT glut = new GLUT();
		
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glColor3d(0.0, 0.0, 1.0);
        gl.glTranslated(location.x, location.y, location.z);
		gl.glRasterPos2d(0, 0);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, this.text);
		gl.glColor3d(1.0, 1.0, 1.0);
		
		gl.glPopMatrix();
	}

}
