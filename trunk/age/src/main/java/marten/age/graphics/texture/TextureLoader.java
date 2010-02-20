package marten.age.graphics.texture;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import marten.age.graphics.image.ImageData;
import marten.age.graphics.util.Dimension;

import org.lwjgl.opengl.GL11;

public final class TextureLoader {
    public static Texture loadTexture(ImageData data) {
        // TODO:zv: not flipping! And why do we need to flip??
        // data = ImageTransformations.flip(data);
        int textureId;
        int pixelType = data.getType();

        ByteBuffer byteBuffer = data.getByteBuffer();

        // TODO:zv: try old solution after everything works
        // IntBuffer textureIdBuffer = IntBuffer.allocate(1);
        IntBuffer textureIdBuffer = ByteBuffer.allocateDirect(4).order(
                ByteOrder.nativeOrder()).asIntBuffer();

        GL11.glGenTextures(textureIdBuffer);
        textureId = textureIdBuffer.get(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
//         GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE,
//         GL11.GL_MODULATE);
//         GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
//         GL11.GL_REPEAT);
//         GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
//         GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
                GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
                GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, pixelType, data.width,
                data.height, 0, pixelType, GL11.GL_UNSIGNED_BYTE, byteBuffer);

        return new Texture(textureId, new Dimension(data.width, data.height));
    }
}
