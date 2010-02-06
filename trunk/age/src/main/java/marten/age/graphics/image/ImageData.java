package marten.age.graphics.image;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import marten.age.graphics.util.Constants;

import org.lwjgl.opengl.GL11;


public class ImageData {
    private byte[] buffer;
    private ByteBuffer byteBuffer;
    public int type;
    public int height;
    public int width;

    public ImageData(String filename) throws IOException {
        this(new File(filename));
    }

    public ImageData(File file) throws IOException {
        this(ImageIO.read(file));
    }

    public ImageData(BufferedImage image) {
        this(((DataBufferByte) (image).getRaster().getDataBuffer()).getData(),
                image.getWidth(), image.getHeight());
    }

    public ImageData(byte[] buffer, int width, int height) {
        int test = 0;

        while (width >> test != 1)
            test++;
        if (width >> test << test != width)
            throw new RuntimeException("Image width is not a power of two.");
        test = 0;
        while (height >> test != 1)
            test++;
        if (height >> test << test != height)
            throw new RuntimeException("Image height is not a power of two.");

        if (buffer.length == width * height * Constants.RGB_NUM_BYTES) {
            this.type = GL11.GL_RGB;
        } else if (buffer.length == width * height * Constants.RGBA_NUM_BYTES) {
            this.type = GL11.GL_RGBA;
        } else {
            throw new RuntimeException("Image data mismatch.");
        }
        this.width = width;
        this.height = height;
        this.buffer = buffer;
        byteBuffer = ByteBuffer.wrap(buffer);
        byteBuffer.rewind();
    }

    public byte[] getBuffer() {
        return this.buffer;
    }

    public int getType() {
        return this.type;
    }

    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }
}
