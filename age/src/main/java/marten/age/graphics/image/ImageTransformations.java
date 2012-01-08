package marten.age.graphics.image;

import marten.age.Constants;

import org.lwjgl.opengl.GL11;

public final class ImageTransformations {

    public static ImageData rotate(ImageData data, int times) {
        ImageData result = data;
        for (int i = 0; i < times; i++) {
            result = ImageTransformations.rotate(result);
        }
        return result;
    }

    public static ImageData rotate(ImageData data) {
        int pixelSize = Constants.RGB_NUM_BYTES;
        if (data.getLwjglPixelType() == GL11.GL_RGBA) {
            pixelSize = Constants.RGBA_NUM_BYTES;
        }

        int width = data.width;
        int height = data.height;
        byte[] buffer = data.getBuffer();
        byte[] newbuffer = new byte[width * height * pixelSize];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int i = 0; i < pixelSize; i++) {
                    newbuffer[(((x + 1) * height - 1) - y) * pixelSize + i]
                              = buffer[(y * width + x) * pixelSize + i];
                }
            }
        }

        return new ImageData(newbuffer, height, width);
    }

    public static ImageData vflip(ImageData data) {
        int pixelSize = Constants.RGB_NUM_BYTES;
        if (data.getLwjglPixelType() == GL11.GL_RGBA) {
            pixelSize = Constants.RGBA_NUM_BYTES;
        }

        int width = data.width;
        int height = data.height;
        byte[] buffer = data.getBuffer();
        byte[] newbuffer = new byte[width * height * pixelSize];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int i = 0; i < pixelSize; i++) {
                    newbuffer[(y * width + (width - x - 1)) * pixelSize + i] =
                        buffer[(y * width + x) * pixelSize + i];
                }
            }
        }

        return new ImageData(newbuffer, width, height);
    }

    public static ImageData flip(ImageData data) {
        int pixelSize = Constants.RGB_NUM_BYTES;
        if (data.getLwjglPixelType() == GL11.GL_RGBA) {
            pixelSize = Constants.RGBA_NUM_BYTES;
        }

        int width = data.width;
        int height = data.height;
        byte[] buffer = data.getBuffer();
        byte[] newbuffer = new byte[width * height * pixelSize];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int i = 0; i < pixelSize; i++) {
                    newbuffer[((height - y - 1) * width + x) * pixelSize + i] =
                        buffer[(y * width + x) * pixelSize + i];
                }
            }
        }

        return new ImageData(newbuffer, width, height);
    }

    public static ImageData blend(ImageData base, ImageData top) {
        return ImageTransformations.blend(base, top, null);
    }

    public static ImageData blend(ImageData base, ImageData top, ImageData mask) {
        if (base.width != top.width || base.height != top.height) {
            throw new RuntimeException("Can not blend images, dimensions do not match");
        }
        int width = base.width;
        int height = base.height;
        int pixelSize = Constants.RGBA_NUM_BYTES;
        byte[] buf1 = base.getBuffer();
        byte[] buf2 = top.getBuffer();
        byte[] maskBuf;
        if (mask == null) {
            maskBuf = buf2;
        } else {
            maskBuf = mask.getBuffer();
        }
        byte[] newBuf = new byte[width * height * pixelSize];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = (y * width + x) * pixelSize;
                float R = (float)(0xFF & buf1[index]) / 255;
                float G = (float)(0xFF & buf1[index + 1]) / 255;
                float B = (float)(0xFF & buf1[index + 2]) / 255;
                float A = (float)(0xFF & buf1[index + 3]) / 255;
                float r = (float)(0xFF & buf2[index]) / 255;
                float g = (float)(0xFF & buf2[index + 1]) / 255;
                float b = (float)(0xFF & buf2[index + 2]) / 255;
                float a = (float)(0xFF & maskBuf[index + 3]) / 255;
                float ax = (byte)(1 - (1 - a) * (1 - A));
                byte rx = (byte)((r * a / ax + R * A * (1 - a) / ax) * 255);
                byte gx = (byte)((g * a / ax + G * A * (1 - a) / ax) * 255);
                byte bx = (byte)((b * a / ax + B * A * (1 - a) / ax) * 255);
                newBuf[index] = rx;
                newBuf[index + 1] = gx;
                newBuf[index + 2] = bx;
                newBuf[index + 3] = (byte)(ax * 255);
            }
        }

        return new ImageData(newBuf, width, height);
    }
}
