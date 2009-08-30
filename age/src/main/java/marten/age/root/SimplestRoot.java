package marten.age.root;

import java.nio.FloatBuffer;


import org.apache.log4j.Logger;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class SimplestRoot extends Root {
	private static org.apache.log4j.Logger log = Logger.getLogger(SimplestRoot.class);
	public SimplestRoot () {
		FloatBuffer pos = FloatBuffer.wrap(new float[] { 5.0f, 5.0f, 10.0f, 0.0f});
		int width = Display.getDisplayMode().getWidth();
		int height = Display.getDisplayMode().getHeight();
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, pos);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		log.info("GL_VENDOR: " + GL11.glGetString(GL11.GL_VENDOR));
		log.info("GL_RENDERER: " + GL11.glGetString(GL11.GL_RENDERER));
		log.info("GL_VERSION: " + GL11.glGetString(GL11.GL_VERSION));
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		float h = (float) height / (float) width;
		GL11.glFrustum(-1.0f, 1.0f, -h, h, 1.0f, 1000.0f);
		GL11.glTranslatef(0.0f, 0.0f, -7.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		super.render();
		GL11.glFlush();
	}
}
