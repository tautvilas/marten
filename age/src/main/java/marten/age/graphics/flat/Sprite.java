package marten.age.graphics.flat;

import marten.age.graphics.BasicSceneGraphChild;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.image.ImageTransformations;
import marten.age.graphics.util.Point;

import org.lwjgl.opengl.GL11;

public class Sprite extends BasicSceneGraphChild {
    private Point position = new Point();
    private ImageData data;

    public Sprite(ImageData data) {
        this.data = ImageTransformations.flip(data);
    }

    public void setPosition(Point point) {
        this.position = point;
    }

    public void setX(double x) {
        this.position.x = x;
    }

    public void setY(double y) {
        this.position.y = y;
    }

    public Point getPosition() {
        return this.position;
    }

    public double getX() {
        return this.position.x;
    }

    public double getY() {
        return this.position.y;
    }

    public int getWidth() {
        return this.data.width;
    }

    public int getHeight() {
        return this.data.height;
    }

    @Override
    public void render() {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glRasterPos2d(position.x, position.y);
        GL11.glDrawPixels((int) data.width, (int) data.height, data.getPixelType(),
                GL11.GL_UNSIGNED_BYTE, data.getByteBuffer());
        GL11.glPopAttrib();
    }
}
