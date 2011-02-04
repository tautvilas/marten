package marten.age.graphics.flat.sprite;

import marten.age.graphics.BasicSceneGraphChild;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.image.ImageTransformations;
import marten.age.graphics.layout.BoxedObject;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;

import org.lwjgl.opengl.GL11;

public class PixelSprite extends BasicSceneGraphChild implements BoxedObject {
    private ImageData data;
    private Point postition = new Point();

    public PixelSprite(ImageData data) {
        this.data = ImageTransformations.flip(data);
    }

    public PixelSprite(ImageData data, Point position) {
        this(data);
        this.setPosition(position);
    }

    @Override
    public Dimension getDimension() {
        return new Dimension(this.data.width, this.data.height);
    }

    @Override
    public void render() {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glRasterPos2d(this.postition.x, this.postition.y);
        GL11.glDrawPixels((int) data.width, (int) data.height, data.getPixelType(),
                GL11.GL_UNSIGNED_BYTE, data.getByteBuffer());
        GL11.glPopAttrib();
    }

    @Override
    public Point getPosition() {
        return this.postition;
    }

    @Override
    public void setPosition(Point point) {
        this.postition = point;
    }
}
