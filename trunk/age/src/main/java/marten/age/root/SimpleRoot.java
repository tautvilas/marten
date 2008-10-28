package marten.age.root;

import java.nio.FloatBuffer;
import java.util.HashMap;

import marten.age.camera.Camera;
import marten.age.hud.Hud;

import org.apache.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class SimpleRoot extends Root {
	private static org.apache.log4j.Logger log = Logger.getLogger(SimpleRoot.class);
	private HashMap<String, Camera> cameras = new HashMap<String, Camera>();
	private HashMap<String, Hud> huds = new HashMap<String, Hud>();
	
	private Camera activeCamera = null;
	private Hud activeHud = null;
	
	public SimpleRoot () {
		FloatBuffer pos = FloatBuffer.wrap(new float[] { 5.0f, 5.0f, 10.0f, 0.0f});
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, pos);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
//		GL11.glEnable(GL11.GL_LINE_SMOOTH);
//		GL11.glEnable(GL11.GL_POINT_SMOOTH);
//		GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
//		GL11.glEnable(GL11.GL_BLEND);
//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA_SATURATE, GL11.GL_ONE);
		
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		log.info("GL_VENDOR: " + GL11.glGetString(GL11.GL_VENDOR));
		log.info("GL_RENDERER: " + GL11.glGetString(GL11.GL_RENDERER));
		log.info("GL_VERSION: " + GL11.glGetString(GL11.GL_VERSION));
	}
	public void addHud(String title, Hud newHud) {
		huds.put(title, newHud);
	}
	public void setActiveHud(String title) {
		this.activeHud = huds.get(title);
	}
	public void addCamera (String title, Camera newCamera) {
		cameras.put(title, newCamera);
	}
	public void setActiveCamera(String title) {
		if (!cameras.containsKey(title))
			throw new RuntimeException ("Attempted to activate non-existant camera.");
		this.activeCamera = cameras.get(title);
	}
	public void activate() {
		if (this.cameras.isEmpty())
			throw new RuntimeException ("Attempted to activate the root without initialized cameras.");
		if (this.activeCamera == null)
			throw new RuntimeException ("Attempted to activate the root without an active camera.");
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		activeCamera.set();
		super.activate();
		if (this.activeHud != null) {
			activeHud.display();
		}
		GL11.glFlush();
	}
}
