package marten.age;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.glu.GLU;

public final class TextureLoader {
	public static Texture loadTexture(String fileName) {
		return loadTexture(new File(fileName));
	}
	
	public static Texture loadTexture(File file) {
		try {
			return loadTexture(ImageIO.read(file));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException ("The image failed to load from the file.");
		}		 
	}
	
	public static Texture loadTexture(BufferedImage image) {
		byte[] bytes = ((DataBufferByte)(image).getRaster().getDataBuffer()).getData();
		return loadTexture(bytes, image.getWidth(), image.getHeight());
	}
	
	public static Texture setImage(ByteBuffer buffer, int width, int height) {
		return loadTexture(buffer.array(), width, height);
	}
	
	public static Texture loadTexture(byte[] buffer, int width, int height) {
		int test = 0;
		int textureId;
		int pixelType;
		
		while (width >> test != 1)
			test++;
		if (width >> test << test != width)
			throw new RuntimeException ("Image width is not a power of two.");
		test = 0;
		while (height >> test != 1)
			test++;
		if (height >> test << test != height)
			throw new RuntimeException ("Image height is not a power of two.");
		
		if (buffer.length == width * height * Constants.RGB_NUM_BYTES) {
			pixelType = GL11.GL_RGB;
		} else if (buffer.length == width * height * Constants.RGBA_NUM_BYTES) {
			pixelType = GL11.GL_RGBA;
		} else {
			throw new RuntimeException ("Image data mismatch.");
		}
		
		buffer = flip(buffer, width, height, pixelType);
		
		/*
		ByteBuffer byteBuffer = BufferUtil.newByteBuffer(buffer.length);
		byteBuffer.put(buffer);
		byteBuffer.rewind();
		*/
		
		ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
		byteBuffer.rewind();
		
//		IntBuffer textureIDBuffer = BufferUtil.newIntBuffer(1);
		
		IntBuffer textureIDBuffer = IntBuffer.allocate(1);
		
		GL11.glGenTextures(textureIDBuffer);
		textureId = textureIDBuffer.get(0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
		GLU.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D, GL11.GL_RGBA, width, height, pixelType, GL11.GL_UNSIGNED_BYTE, byteBuffer);
		// GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, pixelType, width, height, 0, pixelType, GL11.GL_UNSIGNED_BYTE, byteBuffer);
		// TODO: Why does glTexImage2D does not work?
		
		return new Texture(textureId);
	}
	
	@SuppressWarnings("unused")
	private byte[] rotate(byte[] buffer, int width, int height, int pixelType) {
		int pixelSize = Constants.RGB_NUM_BYTES;
		if (pixelType == GL11.GL_RGBA) {
			pixelSize = Constants.RGBA_NUM_BYTES;
		}
		
		byte[] newbuffer = new byte[width * height * pixelSize];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				for (int i = 0; i < pixelSize; i++) {
					newbuffer[(((x + 1) * height - 1) - y) * pixelSize + i] = buffer[(y * width + x) * pixelSize + i];
				}
			}
		}
		
		return newbuffer;
	}
	
	private static byte[] flip(byte[] buffer, int width, int height, int pixelType) {
		int pixelSize = Constants.RGB_NUM_BYTES;
		if (pixelType == GL11.GL_RGBA) {
			pixelSize = Constants.RGBA_NUM_BYTES;
		}
		
		byte[] newbuffer = new byte[width * height * pixelSize];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				for (int i = 0; i < pixelSize; i++) {
					newbuffer[((height - y - 1) * width + x) * pixelSize + i] = buffer[(y * width + x) * pixelSize + i];
				}
			}
		}
		
		return newbuffer;
	}
}
