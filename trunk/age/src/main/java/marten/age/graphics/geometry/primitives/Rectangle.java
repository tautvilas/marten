package marten.age.graphics.geometry.primitives;

import marten.age.graphics.geometry.Geometry;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;

import org.apache.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class Rectangle implements Geometry {
    @SuppressWarnings("unused")
    private static org.apache.log4j.Logger log = Logger
            .getLogger(Rectangle.class);
    private Dimension dimension;
    private Point position = new Point();

    public Rectangle(Dimension dimension) {
        this.dimension = dimension;
    }

    public Rectangle(Dimension dimension, Point position) {
        this(dimension);
        this.position = position;
    }

    @Override
    public void draw() {
//        log.debug("Drawing rectangle " + dimension.width + "x"
//                + dimension.height);
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2d(0, 0);
            GL11.glVertex2d(position.x, position.y);
            GL11.glTexCoord2d(1.0, 0);
            GL11.glVertex2d(position.x + dimension.width, position.y);
            GL11.glTexCoord2d(1.0, 1.0);
            GL11.glVertex2d(position.x + dimension.width, position.y
                    + dimension.height);
            GL11.glTexCoord2d(0, 1.0);
            GL11.glVertex2d(position.x, position.y + dimension.height);
        }
        GL11.glEnd();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Rectangle) {
            Rectangle r2 = (Rectangle) o;
            if (r2.dimension.equals(this.dimension)
                    && r2.position.equals(this.position))
                return true;
            else
                return false;
        } else {
            return super.equals(o);
        }
    }
}
