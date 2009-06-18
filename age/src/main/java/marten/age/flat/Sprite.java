package marten.age.flat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import marten.age.BasicSceneGraphChild;
import marten.age.util.Constants;
import marten.util.Dimension;
import marten.util.Point;

import org.apache.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class Sprite extends BasicSceneGraphChild{
    private static org.apache.log4j.Logger log =
            Logger.getLogger(Sprite.class);

    private Point position = new Point();
    private Dimension dimension = new Dimension();

    ByteBuffer byteBuffer = null;
    int format;

    public Sprite(String fileName) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File(fileName));
        } catch (IOException e) {
            throw new RuntimeException("Image load fail");
        }
        byte[] data = ((DataBufferByte)(image).getRaster().getDataBuffer())
                .getData();
        byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.rewind();
        dimension.width = image.getWidth();
        dimension.height = image.getHeight();
        log.info("Sprite initialized: H: " +
                dimension.height + " W:" + dimension.width + " B:" +
                byteBuffer);
        
        if (data.length == dimension.width *
                dimension.height * Constants.RGB_NUM_BYTES) {
            format = GL11.GL_RGB;
        } else if (data.length == dimension.width * dimension.height
                * Constants.RGBA_NUM_BYTES) {
            format = GL11.GL_RGBA;
        } else {
            throw new RuntimeException("Image data mismatch.");
        }

    }

    public void setPosition(Point point) {
        this.position = point;
    }

    @Override
    public void activate() {
        GL11.glRasterPos2d(position.x, position.y);
        GL11.glDrawPixels((int)dimension.width, (int)dimension.height,
                format, GL11.GL_BYTE, byteBuffer);
    }
}