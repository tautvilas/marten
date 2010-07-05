package marten.age.graphics.texture;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import marten.age.graphics.image.ImageData;
import marten.age.graphics.image.ImageTransformations;
import marten.age.graphics.primitives.Dimension;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public final class TextureLoader {
    public static Texture loadTexture(ImageData data) {
        data = ImageTransformations.flip(data);

        int textureId;
        int pixelType = data.getPixelType();

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

        return new Texture(textureId, new Dimension(data.width, data.height));
    }
}
