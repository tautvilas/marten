package marten.age.flat;

import marten.age.BasicSceneGraphChild;
import marten.age.image.ImageData;
import marten.util.Point;

import org.lwjgl.opengl.GL11;

public class Sprite extends BasicSceneGraphChild{
    private Point position = new Point();
    private ImageData data;

    public Sprite(ImageData data) {
        this.data = data;
    }

    public void setPosition(Point point) {
        this.position = point;
    }

    @Override
    public void render() {
        GL11.glRasterPos2d(position.x, position.y);
        GL11.glDrawPixels((int)data.width, (int)data.height,
                data.getType(), GL11.GL_UNSIGNED_BYTE, data.getByteBuffer());
    }
}