package marten.age.graphics.texture;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import marten.age.graphics.SceneGraphChild;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.image.ImageTransformations;
import marten.age.graphics.primitives.Dimension;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

public final class TextureLoader {

    private static int closestPowerOfTwo(int number) {
        if (number == 0)
            return 1;
        int shift = 0;
        while (number >> shift != 1) {
            shift++;
        }
        if (number >> shift << shift != number) {
            return 1 << shift + 1;
        } else {
            return number;
        }
    }

    public static Texture loadTexture(SceneGraphChild graph, Dimension dimension) {
        int width = (int)dimension.width;
        int height = (int)dimension.height;

        IntBuffer textureIdBuffer = BufferUtils.createIntBuffer(1);
        GL11.glGenTextures(textureIdBuffer);
        int textureId = textureIdBuffer.get(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
                GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
                GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height,
                0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)null);

        IntBuffer buffer = BufferUtils.createIntBuffer(width * height);
        EXTFramebufferObject.glGenFramebuffersEXT(buffer);
        int id = buffer.get();
        EXTFramebufferObject.glBindFramebufferEXT(
                EXTFramebufferObject.GL_FRAMEBUFFER_EXT, id);

        EXTFramebufferObject.glFramebufferTexture2DEXT(
                EXTFramebufferObject.GL_FRAMEBUFFER_EXT,
                EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT,
                GL11.GL_TEXTURE_2D, textureId, 0);

        int framebuffer = EXTFramebufferObject
                .glCheckFramebufferStatusEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT);

        if (framebuffer != EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT) {
            throw new RuntimeException(
                    "Frame buffer is not complete, ogl code:" + framebuffer);
        }

        GL11.glPushAttrib(GL11.GL_VIEWPORT_BIT);
        GL11.glViewport( 0, 0, width, height);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        graph.render();

        EXTFramebufferObject.glBindFramebufferEXT(
                EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);

        GL11.glPopAttrib();

        return new Texture(textureId, dimension);
    }

    public static Texture loadTexture(ImageData data) {
        int width = TextureLoader.closestPowerOfTwo(data.width);
        int height = TextureLoader.closestPowerOfTwo(data.height);
        ImageData origData = data;
        if (data.width != width || data.height != height) {
            int psize = data.getPixelSize();
            byte buffer[] = data.getBuffer();
            byte newData[] = new byte[height * width * psize];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    for (int i = 0; i < psize; i++) {
                        int newY = (y + height - origData.height) % height;
                        int ind = (newY * width + x) * psize + i;
                        if (x >= data.width || y >= data.height) {
                            // fill empty space with white color so that it
                            // would be easier to trace errors
                            newData[ind] = (byte)255;
                        } else {
                            int ind2 = (y * data.width + x) * psize + i;
                            newData[ind] = buffer[ind2];
                        }
                    }
                }
            }
            data = new ImageData(newData, width, height);
        }
        data = ImageTransformations.flip(data);

        int textureId;
        int pixelType = data.getLwjglPixelType();

        ByteBuffer byteBuffer = data.getByteBuffer();

        IntBuffer textureIdBuffer = BufferUtils.createIntBuffer(1);

        GL11.glGenTextures(textureIdBuffer);
        textureId = textureIdBuffer.get(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
                GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
                GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, pixelType, data.width,
                data.height, 0, pixelType, GL11.GL_UNSIGNED_BYTE, byteBuffer);

        return new Texture(textureId, new Dimension(origData.width,
                origData.height), new Dimension(width, height));
    }
}
