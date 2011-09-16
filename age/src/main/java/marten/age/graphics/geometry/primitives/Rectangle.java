package marten.age.graphics.geometry.primitives;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import marten.age.graphics.BasicSceneGraphChild;
import marten.age.graphics.geometry.Geometry;
import marten.age.graphics.layout.BoxedObject;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;

import org.apache.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Rectangle extends BasicSceneGraphChild implements BoxedObject,
        Geometry {
    @SuppressWarnings("unused")
    private static org.apache.log4j.Logger log = Logger
            .getLogger(Rectangle.class);
    private Dimension dimension;
    private Point position = new Point();
    private DoubleBuffer vertices;
    private IntBuffer texVertices;

    public Rectangle(Dimension dimension) {
        this(dimension, new Point());
    }

    public Rectangle(Dimension dimension, Point position) {
        this.dimension = dimension;
        this.position = position;
        vertices = BufferUtils.createDoubleBuffer(8);
        vertices.put(new double[] { position.x, position.y,
                position.x + dimension.width, position.y,
                position.x + dimension.width, position.y + dimension.height,
                position.x, position.y + dimension.height });
        vertices.rewind();
        texVertices = BufferUtils.createIntBuffer(8);
        texVertices.put(new int[] { 0, 0, 1, 0, 1, 1, 0, 1 });
        texVertices.rewind();
    }

    @Override
    public void render() {
        // GL11.glVertexPointer(2, 0, vertices);
        // GL11.glTexCoordPointer(2, 0, texVertices);
        // GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
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
            Rectangle r2 = (Rectangle)o;
            if (r2.dimension.equals(this.dimension)
                    && r2.position.equals(this.position))
                return true;
            else
                return false;
        } else {
            return super.equals(o);
        }
    }

    @Override
    public Dimension getDimension() {
        return this.dimension;
    }

    @Override
    public Point getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(Point position) {
        vertices.put(new double[] { position.x, position.y,
                position.x + dimension.width, position.y,
                position.x + dimension.width, position.y + dimension.height,
                position.x, position.y + dimension.height });
        vertices.rewind();
        this.position = position;
    }
}
