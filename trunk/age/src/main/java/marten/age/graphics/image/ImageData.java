package marten.age.graphics.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import marten.age.Constants;
import marten.age.graphics.primitives.Dimension;

import org.apache.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class ImageData {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(ImageData.class);
    private byte[] buffer;
    private ByteBuffer byteBuffer;
    public int height;
    public int width;

    public ImageData(String filename) throws IOException {
        this(new File(filename));
    }

    public ImageData(File file) throws IOException {
        this(ImageIO.read(file));
    }

    public Dimension getDimension() {
        return new Dimension(this.width, this.height);
    }

    public ImageData(BufferedImage image) {
        int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(),
                null, 0, image.getWidth());
        int type = image.getType();
        if (type == BufferedImage.TYPE_CUSTOM) {
            log.warn("BufferedImage type is TYPE_CUSTOM. "
                    + "Enforncing ABGR type.");
        }
        byte[] data = new byte[pixels.length << 2];
        for (int i = 0; i < pixels.length; i++) {
            int x = pixels[i];
            int j = i << 2;
            if (type == BufferedImage.TYPE_CUSTOM) {
                data[j + 2] = (byte) (x & 0xff);
                data[j + 1] = (byte) ((x >>> 8) & 0xff);
                data[j + 0] = (byte) ((x >>> 16) & 0xff);
                data[j + 3] = (byte) ((x >>> 24) & 0xff);
            } else if (type == BufferedImage.TYPE_4BYTE_ABGR) {
                data[j + 2] = (byte) (x & 0xff);
                data[j + 1] = (byte) ((x >>> 8) & 0xff);
                data[j + 0] = (byte) ((x >>> 16) & 0xff);
                data[j + 3] = (byte) ((x >>> 24) & 0xff);
            } else if (type == BufferedImage.TYPE_3BYTE_BGR) {
                data[j + 2] = (byte) (x & 0xff);
                data[j + 1] = (byte) ((x >>> 8) & 0xff);
                data[j + 0] = (byte) ((x >>> 16) & 0xff);
                data[j + 3] = (byte) 0xff;
            } else if (type == BufferedImage.TYPE_INT_ARGB) {
                data[j + 0] = (byte) (x & 0xff);
                data[j + 1] = (byte) ((x >>> 8) & 0xff);
                data[j + 2] = (byte) ((x >>> 16) & 0xff);
                data[j + 3] = (byte) ((x >>> 24) & 0xff);
            } else if (type == BufferedImage.TYPE_INT_BGR) {
                data[j + 2] = (byte) (x & 0xff);
                data[j + 1] = (byte) ((x >>> 8) & 0xff);
                data[j + 0] = (byte) ((x >>> 16) & 0xff);
                data[j + 3] = (byte) 0xff;
            } else if (type == BufferedImage.TYPE_INT_RGB) {
                data[j + 0] = (byte) (x & 0xff);
                data[j + 1] = (byte) ((x >>> 8) & 0xff);
                data[j + 2] = (byte) ((x >>> 16) & 0xff);
                data[j + 3] = (byte) 0xff;
            } else {
                throw new RuntimeException("Unsupported BufferedImage type: "
                        + type);
            }
            j += 4;
        }
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.buffer = data;
        byteBuffer = BufferUtils.createByteBuffer(height * width * Constants.RGBA_NUM_BYTES);
        byteBuffer.put(data);
        byteBuffer.rewind();
        sanityCheck();
    }

    public ImageData(byte[] buffer, int width, int height) {
        this.width = width;
        this.height = height;
        this.buffer = buffer;
        byteBuffer = BufferUtils.createByteBuffer(height * width * Constants.RGBA_NUM_BYTES);
        byteBuffer.put(buffer);
        byteBuffer.rewind();
        sanityCheck();
    }

    private void sanityCheck() {
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

        if (buffer.length != width * height * Constants.RGBA_NUM_BYTES) {
            throw new RuntimeException("Image data mismatch.");
        }
    }

    public byte[] getBuffer() {
        return this.buffer;
    }

    public int getPixelType() {
        return GL11.GL_RGBA;
    }

    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }
}
