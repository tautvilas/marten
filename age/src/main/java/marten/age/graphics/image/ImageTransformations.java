package marten.age.graphics.image;

import marten.age.graphics.util.Constants;

import org.lwjgl.opengl.GL11;

public final class ImageTransformations {
    public static ImageData rotate(ImageData data) {
        int pixelSize = Constants.RGB_NUM_BYTES;
        if (data.getPixelType() == GL11.GL_RGBA) {
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

        return new ImageData(newbuffer, width, height);
    }

    public static ImageData flip(ImageData data) {
        int pixelSize = Constants.RGB_NUM_BYTES;
        if (data.getPixelType() == GL11.GL_RGBA) {
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
}
