package marten.testing.visualization.jogl;

import javax.media.opengl.GL;

public interface LightSource {
	public void setLight (GL gl, int lightNumber);
}
