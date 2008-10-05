package marten.testing.visualization.jogl;

import javax.media.opengl.GL;

public abstract class Model {
	public static int subLoRes = 0;
	public static int loRes = 1;
	public static int standard = 2;
	public static int hiRes = 3;
	public static int superHiRes = 4;
	public static double noDrawThreshold = 250000.0;
	public static double subLoResThreshold = 16384.0;
	public static double loResThreshold = 4096.0;
	public static double standardThreshold = 1024.0;
	public static double hiResThreshold = 256.0;
	protected int[] model = {-1, -1, -1, -1, -1};
	abstract public void generate(GL gl);
	public void display(GL gl, int resolution) {
		if (resolution < 0 || resolution > 4)
			throw new RuntimeException ("Attempted to draw a model of undefined resolution.");
		if (model[resolution] == -1)
			this.generate(gl);
		gl.glCallList(model[resolution]);
	}
}
