package marten.age.graphics.appearance;

public final class Color {
	public double r;
	public double g;
	public double b;
	public double a;
	
	public Color (double red, double green, double blue) {
		this(red, green, blue, 1.0);
	}
	public Color (double red, double green, double blue, double alpha) {
		this.r = red;
		this.g = green;
		this.b = blue;
		this.a = alpha;
	}
	public Color toGreyscale () {
		double intensity = 0.3 * this.r + 0.59 * this.g + 0.11 * this.b;
		return new Color (intensity, intensity, intensity, this.a);
	}
	public Color toSepia () {
		// An obvious cheating, as technically colors should be in 0.0-1.0 interval, but it doesn't matter much.
		return this.toSepia(new Color(1.3, 1.15, 0.9));
	}
	public Color toSepia (Color tone) {
		Color greyscale = this.toGreyscale();
		return new Color (greyscale.r * tone.r, greyscale.g * tone.g, greyscale.b * tone.b, greyscale.a * tone.a);
	}
	public Color toColorToned (Color tone) {
		return new Color (this.r * tone.r, this.g * tone.g, this.b * tone.b, this.a * tone.a);
	}
	public Color negate () {
		return new Color (1 - this.r, 1 - this.g, 1 - this.b, this.a);
	}
	public String toString () {
		return "Red: " + this.r + "\nGreen: " + this.g + "\nBlue: " + this.b + "\nAlpha: " + this.a + "\n";
	}
}
