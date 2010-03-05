package marten.age.graphics.root;

import java.nio.FloatBuffer;
import java.util.HashMap;

import marten.age.graphics.camera.Camera;

import org.apache.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class SimpleRoot extends Root {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(SimpleRoot.class);
    private HashMap<String, Camera> cameras = new HashMap<String, Camera>();

    private Camera activeCamera = null;

    public SimpleRoot() {
        FloatBuffer pos = FloatBuffer.wrap(new float[] { 5.0f, 5.0f, 10.0f,
                0.0f });
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, pos);
        // XXX(zv): it is strange that org.lwjgl.opengl.glu.Sphere is drawn CW
        // GL11.glEnable(GL11.GL_CULL_FACE);
        // GL11.glCullFace(GL11.GL_FRONT);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_LIGHT0);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_NORMALIZE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        // GL11.glEnable(GL11.GL_LINE_SMOOTH);
        // GL11.glEnable(GL11.GL_POINT_SMOOTH);
        // GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
        // GL11.glEnable(GL11.GL_BLEND);
        // GL11.glBlendFunc(GL11.GL_SRC_ALPHA_SATURATE, GL11.GL_ONE);
        // GL11.glDepthFunc(GL11.GL_LEQUAL);

        log.info("GL_VENDOR: " + GL11.glGetString(GL11.GL_VENDOR));
        log.info("GL_RENDERER: " + GL11.glGetString(GL11.GL_RENDERER));
        log.info("GL_VERSION: " + GL11.glGetString(GL11.GL_VERSION));
    }

    public void addCamera(String title, Camera newCamera) {
        cameras.put(title, newCamera);
    }

    public void setActiveCamera(String title) {
        if (!cameras.containsKey(title))
            throw new RuntimeException(
                    "Attempted to activate non-existant camera.");
        this.activeCamera = cameras.get(title);
    }

    public void render() {
        if (this.cameras.isEmpty())
            throw new RuntimeException(
                    "Attempted to activate the root without initialized cameras.");
        if (this.activeCamera == null)
            throw new RuntimeException(
                    "Attempted to activate the root without an active camera.");
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        activeCamera.set();
        super.render();
        GL11.glFlush();
    }
}
