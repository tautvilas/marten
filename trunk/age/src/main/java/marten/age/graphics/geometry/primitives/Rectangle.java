package marten.age.graphics.geometry.primitives;

import marten.age.graphics.geometry.Geometry;
import marten.age.graphics.util.Dimension;

import org.lwjgl.opengl.GL11;

public class Rectangle implements Geometry {
    private Dimension dimension;

    public Rectangle(Dimension dimension) {
        this.dimension = dimension;
    }

    @Override
    public void draw() {
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glNormal3f(0, 0, -1);
            GL11.glTexCoord2d(0, 0);
            GL11.glVertex2d(0, 0);
            GL11.glTexCoord2d(1.0, 0);
            GL11.glVertex2d(dimension.width, 0);
            GL11.glTexCoord2d(1.0, 1.0);
            GL11.glVertex2d(dimension.width, dimension.height);
            GL11.glTexCoord2d(0, 1.0);
            GL11.glVertex2d(0, dimension.height);
        }
        GL11.glEnd();
    }
}
