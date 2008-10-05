package marten.age.texgen.util;

public final class Noise {
	public static double interpolateLinear(double pointA, double pointB, double distance) {
		if (distance < 0 || distance > 1) {
			throw new RuntimeException("Distance must be between 0 and 1 inclusive");
		}
		return  pointA * (1 - distance) + pointB * distance;
	}

	public static double interpolateCosine(double pointA, double pointB, double distance) {
		if (distance < 0 || distance > 1) {
			throw new RuntimeException("Distance must be between 0 and 1 inclusive");
		}
		double ft = distance * Math.PI;
		double f = (1 - Math.cos(ft)) * .5;
		return  pointA * (1 - f) + pointB * f;
	}

	public static double interpolateCubic(double prevA, double pointA, double pointB, double postB, double distance) {
		if (distance < 0 || distance > 1) {
			throw new RuntimeException("Distance must be between 0 and 1 inclusive");
		}
		double p = (postB - pointB) - (prevA - pointA);
		double q = (prevA - pointA) - p;
		double r = pointB - prevA;
		double s = pointA;

		return p * Math.pow(distance, 3) + q * Math.pow(distance, 2) + r * distance + s;
	}

	public static double noise(int x)
	{
		x = (x << 13) ^ x;
		return (((x * (x * x * 15731 + 789221) + 1376312589) & 0x7fffffff) / 2147483648.0);
	}

	public static double smoothedNoise(int x) {
		return noise(x) / 2 + noise(x - 1) / 4 + noise(x + 1) / 4;
	}

	public static double interpolatedNoise(double x) {
		int integerX  = (int)x;
		double fractionalX = x - integerX;

		double v1 = smoothedNoise(integerX);
		double v2 = smoothedNoise(integerX + 1);

		return interpolateCosine(v1 , v2 , fractionalX);
	}

	public static double perlinNoise(double x, int octaves, double persistence) {
		double total = 0;

		for (int i = 0; i < octaves; i++) {
			double frequency = Math.pow(2, i);
			double amplitude = Math.pow(persistence, i);
			total = total + interpolatedNoise(x * frequency) * amplitude;
		}
		return total;
	}
}
