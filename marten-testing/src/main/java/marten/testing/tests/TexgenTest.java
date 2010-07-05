package marten.testing.tests;

import java.io.File;
import java.nio.Buffer;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import marten.age.core.SelectionDialog;
import marten.age.graphics.texture.TextureProvider;
import marten.testing.Constants;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

public class TexgenTest implements GLEventListener {
	
	private TextureProvider textureProvider;
	private Texture texture;
	private Buffer buffer;
	private double rotate = -180;
	private GLU glu;
	
	public TexgenTest() {
		textureProvider = selectTexture();
		if (textureProvider == null) throw new RuntimeException("No texture selected");
	}

	public void display(GLAutoDrawable drawable) {
		final GL gl = drawable.getGL();

		if ((drawable instanceof GLJPanel) &&
				!((GLJPanel) drawable).isOpaque() &&
				((GLJPanel) drawable).shouldPreserveColorBufferIfTranslucent()) {
			gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		} else {
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		}

		gl.glMatrixMode(GL.GL_PROJECTION);
		
		gl.glPushMatrix();

		gl.glRasterPos2i(-1, 1);
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glDrawPixels(textureProvider.getWidth(), textureProvider.getHeight(), GL.GL_RGB,
				GL.GL_UNSIGNED_BYTE, buffer);
		gl.glFlush();
		gl.glEnable(GL.GL_DEPTH_TEST);
		
	    gl.glTranslated(-0.8, -0.6, -8.0);
	    gl.glMatrixMode(GL.GL_MODELVIEW);
	    gl.glPushMatrix();
	    gl.glLoadIdentity();
	    
	    gl.glRotated(rotate, 0.0, 1.0, 0.0);
	    gl.glRotated(-90, 1.0, 0.0, 0.0);
		rotate = (rotate + 1.0);
		if (rotate == 180) rotate = -180;
	    
	    GLUquadric quadratic= glu.gluNewQuadric();
		glu.gluQuadricNormals(quadratic, GLU.GLU_SMOOTH);
		glu.gluQuadricTexture(quadratic, true);
		texture.enable();
		texture.bind();
		glu.gluSphere(quadratic, 0.5, 50, 25);
		texture.disable();
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPopMatrix();
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
		// Unimplemented in JOGL
	}

	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		glu = new GLU();
		
		float pos[] = { 5.0f, 5.0f, 10.0f, 0.0f };
		
		gl.glClearColor (0.0f, 0.0f, 0.0f, 0.0f);
		
		gl.setSwapInterval(1);
		
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, pos, 0);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glEnable(GL.GL_DEPTH_TEST);
		
		textureProvider.generate();
		texture = TextureIO.newTexture(textureProvider.getBufferedImage(), true);
		buffer = textureProvider.getBuffer();
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL gl = drawable.getGL();

		if (width <= 0) width = 1;

		float h = (float)height / (float)width;

		gl.glMatrixMode(GL.GL_PROJECTION);

		System.err.println("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
		System.err.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
		System.err.println("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));
		gl.glLoadIdentity();
		gl.glFrustum(-1.0f, 1.0f, -h, h, 5.0f, 1e30);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, -40.0f);
	}
	
	private TextureProvider selectTexture() {
		File dir = new File(Constants.GENERATED_TEXTURES_PATH);
		String[] children = dir.list();
		
		ArrayList<String> classNames = new ArrayList<String>();
		for(int i = 0; i < children.length; i++) {
			String[] tokens = children[i].split("\\.");
			// System.out.println(tokens[0]);
			if (tokens.length == 2 && tokens[1].equals("java")) {
				classNames.add(tokens[0]);
			}
		}
		
		String[] ids = new String[classNames.size()];
		for(int i = 0; i < classNames.size(); i++) {
			ids[i] = classNames.get(i);
		}
		
		SelectionDialog dialog = new SelectionDialog("Texture selection", ids);
	    dialog.setVisible(true);
	    dialog.waitFor();
	    if (dialog.cancelWasPressed()) return null;
	    String classname = ids[dialog.selectedIndex()];
	    
	    Class<?> clas = null;
	    Object instance = null;
	    try {
	    	clas = Class.forName(Constants.GENERATED_TEXTURES_PACKAGE + "." + classname);
	    	instance = clas.newInstance();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	throw new RuntimeException("Could not load test class " + classname);
	    }
	    
	    if (instance instanceof TextureProvider) {
	    	TextureProvider provider = (TextureProvider)instance;
	    	return provider;
	    } else {
	    	throw new RuntimeException("Unsuported texture type");
	    }
	}

}
