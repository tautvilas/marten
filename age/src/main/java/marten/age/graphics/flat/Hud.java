package marten.age.graphics.flat;

import marten.age.graphics.BasicSceneGraphBranch;
import marten.age.graphics.SceneGraphNode;
import marten.age.graphics.primitives.Point;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class Hud extends BasicSceneGraphBranch {

    private Point position = new Point();

    public void moveDown(int numPixels) {
        this.position.y -= numPixels;
    }

    @Override
    public void render() {
        int width = Display.getDisplayMode().getWidth();
        int height = Display.getDisplayMode().getHeight();

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0, width, height, 0, -1, 1);
        GL11.glScalef(1, -1, 1);
        GL11.glTranslatef(0, -height, 0);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();

        GL11.glTranslated(position.x, position.y, 0);

        for (SceneGraphNode node : this.getBranches()) {
            node.render();
        }

        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        GL11.glPopAttrib();
    }
}
