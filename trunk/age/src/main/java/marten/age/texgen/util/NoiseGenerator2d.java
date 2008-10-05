package marten.age.texgen.util;

public class NoiseGenerator2d {
	private int width;
	private int seed;

	public NoiseGenerator2d(int width, int seed) {
		this.width = width;
		this.seed = seed;
	}

	public double noise2d(int x, int y)
	{
		return Noise.noise((int)x + (int)(y) * (width + seed));
	}

	public double smoothedNoise2d(int x, int y) {
		double corners = (noise2d(x - 1, y - 1) + noise2d(x + 1, y - 1) + noise2d(x - 1, y + 1)+noise2d(x + 1, y + 1) ) / 16;
		double sides   = (noise2d(x - 1, y) + noise2d(x + 1, y) + noise2d(x, y - 1) + noise2d(x, y + 1)) / 8;
		double center  =  noise2d(x, y) / 4;
		return corners + sides + center;
	}

	public double interpolatedNoise2d(double x, double y, boolean smoothed) {
		int intX = (int)x;
		double fractionalX = x - intX;

		int intY = (int)y;
		double fractionalY = y - intY;

		double v1, v2, v3, v4;

		if (smoothed) {
			v1 = smoothedNoise2d(intX, intY);
			v2 = smoothedNoise2d(intX + 1, intY);
			v3 = smoothedNoise2d(intX, intY + 1);
			v4 = smoothedNoise2d(intX + 1, intY + 1);
		} else {
			v1 = noise2d(intX, intY);
			v2 = noise2d(intX + 1, intY);
			v3 = noise2d(intX, intY + 1);
			v4 = noise2d(intX + 1, intY + 1);
		}

		double i1 = Noise.interpolateLinear(v1 , v2 , fractionalX);
		double i2 = Noise.interpolateLinear(v3 , v4 , fractionalX);

		return Noise.interpolateCosine(i1 , i2 , fractionalY);
	}

	public double perlinNoise2d(double x, double y, double persistence, int octaves) {	
		double total = 0;

		for (int i = 0; i < octaves; i ++) {
			double frequency = Math.pow(2, i);
			double amplitude = Math.pow(persistence, i);

			total = total + interpolatedNoise2d(x * frequency, y * frequency, true) * amplitude;
		}
		
		return total;
	}
}
