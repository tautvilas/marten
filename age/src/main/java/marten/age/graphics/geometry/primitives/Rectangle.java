package marten.age.graphics.geometry.primitives;

import marten.age.graphics.BasicSceneGraphChild;
import marten.age.graphics.geometry.Geometry;
import marten.age.graphics.layout.BoxedObject;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.primitives.TextureCoords;

import org.apache.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class Rectangle extends BasicSceneGraphChild implements BoxedObject,
        Geometry {
    @SuppressWarnings("unused")
    private static org.apache.log4j.Logger log = Logger
            .getLogger(Rectangle.class);
    private Dimension dimension;
    private Point position = new Point(0, 0);
    private Point texPos = new Point(0, 0);
    private Dimension texDim = new Dimension(1, 1);

    // private DoubleBuffer vertices;
    // private IntBuffer texVertices;

    public Rectangle(Dimension dimension, TextureCoords coords) {
        this(new Point(), dimension, coords);
    }

    public Rectangle(Dimension dimension) {
        this(new Point(), dimension, new TextureCoords());
    }

    public Rectangle(Dimension dimension, Point position) {
        this(position, dimension, new TextureCoords());
    }

    public Rectangle(Point position, Dimension dimension, TextureCoords coords) {
        this.dimension = dimension;
        this.position = position;
        this.texPos = coords.getPosition();
        this.texDim = coords.getDimension();
        System.out.println(this.texPos);
        System.out.println(this.texDim);
        // vertices = BufferUtils.createDoubleBuffer(8);
        // vertices.put(new double[] { position.x, position.y,
        // position.x + dimension.width, position.y,
        // position.x + dimension.width, position.y + dimension.height,
        // position.x, position.y + dimension.height });
        // vertices.rewind();
        // texVertices = BufferUtils.createIntBuffer(8);
        // texVertices.put(new int[] { 0, 0, 1, 0, 1, 1, 0, 1 });
        // texVertices.rewind();
    }

    @Override
    public void render() {
        // GL11.glVertexPointer(2, 0, vertices);
        // GL11.glTexCoordPointer(2, 0, texVertices);
        // GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2d(this.texPos.x, this.texPos.y);
            GL11.glVertex2d(this.position.x, this.position.y);
            GL11.glTexCoord2d(this.texPos.x + this.texDim.width, this.texPos.y);
            GL11.glVertex2d(this.position.x + this.dimension.width,
                    this.position.y);
            GL11.glTexCoord2d(this.texPos.x + this.texDim.width, this.texPos.y
                    + this.texDim.height);
            GL11.glVertex2d(this.position.x + this.dimension.width,
                    this.position.y + this.dimension.height);
            GL11.glTexCoord2d(this.texPos.x, this.texPos.y + this.texDim.height);
            GL11.glVertex2d(this.position.x, this.position.y
                    + this.dimension.height);
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
        // vertices.put(new double[] { position.x, position.y,
        // position.x + dimension.width, position.y,
        // position.x + dimension.width, position.y + dimension.height,
        // position.x, position.y + dimension.height });
        // vertices.rewind();
        this.position = position;
    }
}
