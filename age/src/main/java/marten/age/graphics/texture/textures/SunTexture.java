package marten.age.graphics.texture.textures;

import marten.age.graphics.appearance.Color;
import marten.age.graphics.texture.TextureProvider;
import marten.age.graphics.texture.util.NoiseGenerator2d;

public class SunTexture extends TextureProvider {
	private Color baseColor = new Color(1.0, 1.0, 1.0);
	private int seed = 7;
	private NoiseGenerator2d generator = null;

	public SunTexture(int width, int height) {
		super(width, height);
	}
	
	public SunTexture(){
		super();
	}

	@Override
	protected void generateTexture() {
		int height = this.getHeight();
		int width = this.getWidth();
		
		generator = new NoiseGenerator2d(width, seed);
		
		double scale = 2.0;

		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				double value = generator.perlinNoise2d(x / scale, y / scale, 1 / 5.0, 2);
				
				if (value < 0.8) {
					value = 1.0;
				} else {
					value = (1.0 - value) * 5.0;
				}
				
				int r = (int)(baseColor.r * value);
				int g = (int)(baseColor.g * value);
				int b = (int)(baseColor.b * value);
				
				Color p = new Color(r, g, b);
				this.setPixel(p, x, y);
			}
		}
		
		// normalizeSphereTexture();
	}
}
