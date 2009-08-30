package marten.age.flat;

import marten.age.BasicSceneGraphBranch;
import marten.age.SceneGraphNode;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class Hud extends BasicSceneGraphBranch {
    @Override
    public void render() {
        int width = Display.getDisplayMode().getWidth();
        int height = Display.getDisplayMode().getHeight();

        boolean lightingEnabled = GL11.glIsEnabled(GL11.GL_LIGHTING);
        boolean blendEnabled = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean depthEnabled = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);

//         GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        // GLU.gluOrtho2D(0, width, 0, height);
        GL11.glOrtho(0, width, height, 0, -1, 1);
        GL11.glScalef(1, -1, 1);
        GL11.glTranslatef(0, -height, 0);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();

        for (SceneGraphNode node : this.getBranches()) {
            node.render();
        }

        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        if (lightingEnabled)
            GL11.glEnable(GL11.GL_LIGHTING);
        else
            GL11.glDisable(GL11.GL_LIGHTING);
        if (depthEnabled)
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        else
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        if (!blendEnabled)
            GL11.glDisable(GL11.GL_BLEND);
        else
            GL11.glDisable(GL11.GL_BLEND);
    }
}
