package marten.age.texture.textures;

import marten.age.texture.TextureProvider;
import marten.age.texture.util.Noise;
import marten.age.util.Color;

public class SaturnTexture extends TextureProvider {
	private Color baseColor = new Color(0xe1 / 255.0, 0xbe / 255.0, 0x44 / 255.0);
	private int baseLineThickness = 5;
	// private int thicknessDrift = 30;
	private double colorDrift = 20 / 255.0 ;
	private int seed = 10;
	
	public SaturnTexture() {
		super();
	}
	
	public SaturnTexture(int width, int height) {
		super(width, height);
	}

	@Override
	public void generateTexture() {
		double height = this.getHeight();
		double width = this.getWidth();
		
		double bx = baseColor.r;
		
		double delta = 0.0;
		double prevdelta = 0.0;
		int thickness = baseLineThickness;
		for(int y = 0; y < height; y++) {
			if (y % thickness == 0) {
				prevdelta = delta;
				// thickness = (int)baseLineThickness + (int)(baseLineThickness * thicknessDrift * (200.0 * Math.random() - 100.0) / 10000.0);
				delta = (Noise.noise(seed + y) * 1.0);
			}
			for(int x = 0; x < width; x++){
				double c = Noise.interpolateCosine(prevdelta, delta, 1.0 / thickness * (y % thickness));

				int r = (int)(bx + (bx * colorDrift * (c - 100) / 10000.0));
				int g = (int)(baseColor.g + (baseColor.g * colorDrift * (c - 100) / 10000.0));
				int b = (int)(baseColor.b + (baseColor.b * colorDrift * (c - 100) / 10000.0));
				
				Color p = new Color(r, g, b);
				this.setPixel(p, x, y);
			}
		}
	}
}
