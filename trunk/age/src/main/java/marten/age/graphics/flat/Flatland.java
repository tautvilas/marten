package marten.age.graphics.flat;

import marten.age.core.AppInfo;
import marten.age.graphics.root.Root;

import org.apache.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class Flatland extends Root {

    private static org.apache.log4j.Logger log = Logger
            .getLogger(Flatland.class);

    public Flatland() {
        log.info("GL_VENDOR: " + GL11.glGetString(GL11.GL_VENDOR));
        log.info("GL_RENDERER: " + GL11.glGetString(GL11.GL_RENDERER));
        log.info("GL_VERSION: " + GL11.glGetString(GL11.GL_VERSION));
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, AppInfo.getDisplayWidth(), AppInfo.getDisplayHeight(),
                0, 0, 1);
        GL11.glScalef(1, -1, 1);
        GL11.glTranslatef(0, -AppInfo.getDisplayHeight(), 0);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        super.render();
        GL11.glFlush();
    }
}
