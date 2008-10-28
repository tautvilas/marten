package marten.age.texture;

import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import marten.age.util.Color;

public abstract class TextureProvider {
	private static final int DEFAULT_WIDTH = 512;
	private static final int DEFAULT_HEIGHT = 256;
	
	private byte[] image;
	private BufferedImage bufferedImage;
	private int width = 0;
	private int height = 0;
	private boolean generated = false;
	
	public TextureProvider() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	public TextureProvider(int width, int height) {
		this.width = width;
		this.height = height;
		image = new byte[this.height * this.width * Constants.RGB_NUM_BYTES];
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Buffer getBuffer(){
		if (generated) {
			ByteBuffer buffer = ByteBuffer.allocate(this.height * this.width * Constants.RGB_NUM_BYTES);
			buffer.put(image);
			buffer.rewind();
			return buffer;
		} else {
			throw new RuntimeException("Texture not yet generated");
		}
	}

	private void generateBufferedImage() {
		DataBuffer db = new DataBufferByte(image, image.length);
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
		ColorModel cm = new ComponentColorModel(cs, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		WritableRaster raster = Raster.createInterleavedRaster(db, width, height, width * Constants.RGB_NUM_BYTES,
				Constants.RGB_NUM_BYTES, new int[]{0, 1, 2}, null);
		bufferedImage = new BufferedImage(cm, raster, false, null);
	}

	public BufferedImage getBufferedImage() {
		if (generated) return bufferedImage;
		else {
			throw new RuntimeException("Buffered image is not yet generated so it can not be returned");
		}
	}

	public void generate() {
		generateTexture();
		generateBufferedImage();
		generated = true;
	}
	
	protected void normalizeSphereTexture() {

		byte[] imageBackup = image.clone();
		
		byte newr = 0;
		byte newg = 0;
		byte newb = 0;

		for (int j=0; j < height; j++) {
			double theta = Math.PI * (j - (height - 1) / 2.0) / (double)(height - 1);
			for (int i = 0; i < width; i++) {
				double phi  = (Math.PI * 2.0) * (i - width / 2.0) / (double)width;
				double phi2 = phi * Math.cos(theta);
				double i2  = (phi2 * width) / (Math.PI * 2.0) + width / 2.0;
				if (i2 < 0 || i2 > width - 1) {
					newr = (byte)0xff;                         /* Should not happen */
				} else {
					int position = (int)width * j * Constants.RGB_NUM_BYTES + (int)i2 * Constants.RGB_NUM_BYTES;
					
					newr = imageBackup[position];
					newg = imageBackup[position + 1];
					newb = imageBackup[position + 2];
				}
				int position = (int)width * j * Constants.RGB_NUM_BYTES + i * Constants.RGB_NUM_BYTES;
				
				image[position] = newr;
				image[position + 1] = newg;
				image[position + 2] = newb;
			}
		}
	}
	
	protected void setPixel(Color p, int x, int y) {
		int position = (int)width * y * Constants.RGB_NUM_BYTES + x * Constants.RGB_NUM_BYTES;
		
		image[position] = (byte)(p.r * 255.0);
		image[position + 1] = (byte)(p.g * 255.0);
		image[position + 2] = (byte)(p.b * 255.0);
	}

	protected abstract void generateTexture();
}
