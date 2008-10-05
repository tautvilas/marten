package marten.testing.visualization.jogl;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;


public class SphericModel extends Model {

	@Override
	public void generate(GL gl) {
		int startOfRange = gl.glGenLists(5);
		float[] white = {1.0f, 1.0f, 1.0f, 1.0f};
		for (int index = 0, resolution = 4; index < 5; index++, resolution *= 2) {
			this.model[index] = startOfRange + index;
			gl.glNewList(this.model[index], GL.GL_COMPILE);
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, white, 0);
			GLU glu = new GLU();
			GLUquadric quadratic= glu.gluNewQuadric();
			glu.gluQuadricNormals(quadratic, GLU.GLU_SMOOTH);
			glu.gluQuadricTexture(quadratic, true);		
			glu.gluSphere(quadratic, 1, resolution * 2, resolution);
			gl.glEndList();
		}
	}
}
