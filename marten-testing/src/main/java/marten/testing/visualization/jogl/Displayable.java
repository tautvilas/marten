package marten.testing.visualization.jogl;

import javax.media.opengl.GL;

import marten.util.Point;



public interface Displayable {
	public void displaySolid (GL gl);
	public void displaySolid (GL gl, Point camera);
	public void displayTransparent (GL gl);
	public void displayTransparent (GL gl, Point camera);
	public abstract void displayTextInfo (GL gl);	
}
