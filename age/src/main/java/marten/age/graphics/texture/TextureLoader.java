package marten.age.graphics.texture;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import marten.age.graphics.image.ImageData;
import marten.age.graphics.image.ImageTransformations;
import marten.util.Dimension;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.glu.GLU;

public final class TextureLoader {
    public static Texture loadTexture(ImageData data) {
        data = ImageTransformations.flip(data);
        int textureId;
        int pixelType = data.getType();
        int width = data.width;
        int height = data.height;

        ByteBuffer byteBuffer = data.getByteBuffer();

        IntBuffer textureIDBuffer = IntBuffer.allocate(1);

        GL11.glGenTextures(textureIDBuffer);
        textureId = textureIDBuffer.get(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE,
                GL11.GL_MODULATE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
                GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
                GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
                GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
                GL11.GL_LINEAR_MIPMAP_NEAREST);
        GLU.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D, GL11.GL_RGBA, width, height,
                pixelType, GL11.GL_UNSIGNED_BYTE, byteBuffer);
//         GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, pixelType, width, height, 0,
//         pixelType, GL11.GL_UNSIGNED_BYTE, byteBuffer);
        // TODO(zv): why does glTexImage2D does not work?

        return new Texture(textureId, new Dimension(width, height));
    }
}
