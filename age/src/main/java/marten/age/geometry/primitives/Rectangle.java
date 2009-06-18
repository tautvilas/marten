package marten.age.geometry.primitives;

import marten.age.geometry.Geometry;
import marten.util.Dimension;

import org.lwjgl.opengl.GL11;

public class Rectangle implements Geometry {
    private Dimension dimension;

    public Rectangle(Dimension dimension) {
        this.dimension = dimension;
    }

    @Override
    public void draw() {
        // TODO(zv): do we need to push here?
        GL11.glPushMatrix();
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2f(0, dimension.height);
            GL11.glVertex2f(0, dimension.height);
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2f(0, 0);
            GL11.glTexCoord2f(dimension.width, 0);
            GL11.glVertex2f(dimension.width, 0);
            GL11.glTexCoord2f(dimension.width, dimension.height);
            GL11.glVertex2f(dimension.width, dimension.height);
        }
        GL11.glEnd();
        GL11.glPopMatrix();
    }
}
