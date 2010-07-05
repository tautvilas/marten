package marten.age.graphics.texture.textures;

import marten.age.graphics.appearance.Color;
import marten.age.graphics.texture.TextureProvider;
import marten.age.graphics.texture.util.NoiseGenerator2d;

public class EarthTexture extends TextureProvider {
	private static final int DEFAULT_SEED = 3;
	private int seed = DEFAULT_SEED;
	
	public EarthTexture() {
		super();
	}
	
	public EarthTexture(int width, int height, int seed) {
		super(width, height);
		this.seed = seed;
	}
	
	public EarthTexture(int width, int height) {
		this(width, height, DEFAULT_SEED);
	}
	
	public void generateTexture() {
		int width = this.getWidth();
		int height = this.getHeight();
		
		NoiseGenerator2d ng = new NoiseGenerator2d(width, seed);
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				double ydelta = height / 2 - y;
				if (ydelta == 0) ydelta++;
				double xv = x;
				if (x > width / 2) {
					xv = width - 1 - x;
				}
				double yv = y;

				xv = xv / 30.0;
				yv = yv / 30.0;
				double value1 = ng.perlinNoise2d(xv, yv, 1.0 / 1.6, 4);

				Color p = new Color(value1, value1, (value1 / 2.0));
				this.setPixel(p, x, y);
			}
		}
		
		// makeTextureTileHorizontaly(ng);
		normalizeSphereTexture();
	}
	
	/*
	protected void makeTextureTileHorizontaly(NoiseGenerator2d ng) {
		int width = this.getWidth();
		int height = this.getHeight();
		byte[] image = this.getImage();
		
		for(int x = 0; x < width; x++){
			for (int y = height; y < height + (int)(height / 5 * Noise.perlinNoise(x / 40.0, 4, 1.0 / 2.0
					)); y++) {
				double xv = x;
				double yv = y;

				xv = xv / 30.0;
				yv = yv / 30.0;

				double value1 = ng.perlinNoise2d(xv, yv, 1.0 / 1.6, 4);
				double color = (value1) * 255.0;
				int position = width * (y - height) * GlobalConstants.RGB_NUM_BYTES + x * GlobalConstants.RGB_NUM_BYTES;
				image[position] = (byte)(color);
				image[position + 1] = (byte)(color);
				image[position + 2] = (byte)(color / 2.0);
			}
		}
	}
	*/
}
