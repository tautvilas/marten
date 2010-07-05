package marten.age.graphics.light;

import java.nio.FloatBuffer;

import marten.age.graphics.appearance.Color;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.primitives.Vector;

import org.lwjgl.opengl.GL11;


public class Light {
	private FloatBuffer specular = FloatBuffer.wrap(new float[] {0.0f, 0.0f, 0.0f, 1.0f});
	private FloatBuffer diffuse = FloatBuffer.wrap(new float[] {1.0f, 1.0f, 1.0f, 1.0f});
	private FloatBuffer ambient = FloatBuffer.wrap(new float[] {1.0f, 1.0f, 1.0f, 1.0f});
	private FloatBuffer position = FloatBuffer.wrap(new float[] {0.0f, 0.0f, 1.0f, 1.0f});
	private FloatBuffer direction = FloatBuffer.wrap(new float[] {0.0f, 0.0f, -1.0f, 1.0f});
	private float spotExponent = 0.0f;
	private float spotCutoff = 180.0f;
	private float constantAttenuation = 1.0f;
	private float linearAttenuation = 0.0f;
	private float quadraticAttenuation = 0.0f;
	
	public Light () {}
	
	public Light (Color newSpecular, Color newDiffuse, Color newAmbient, Point newPosition, Vector newDirection, double newExponent, double newCutoff,
			double newConstant, double newLinear, double newQuadratic) {
		this.setAmbient(newAmbient);
		this.setAttenuation(newConstant, newLinear, newQuadratic);
		this.setDiffuse(newDiffuse);
		this.setDirection(newDirection);
		this.setPosition(newPosition);
		this.setSpecular(newSpecular);
		this.setSpot(newCutoff, newExponent);
	}
	
	public void setSpecular (Color newSpecular) {
		float[] buffer = {(float)newSpecular.r, (float)newSpecular.g, (float)newSpecular.b, (float)newSpecular.a};
		for (float channel : buffer) {
			if (channel < 0.0f)
				channel = 0.0f;
			else if (channel > 1.0f)
				channel = 1.0f;
		}
		this.specular = FloatBuffer.wrap(buffer);
	}
	
	public Color getSpecular () {
		return new Color (this.specular.array()[0], this.specular.array()[1], this.specular.array()[2], this.specular.array()[3]);
	}
	
	public void setDiffuse (Color newDiffuse) {
		float[] buffer = {(float)newDiffuse.r, (float)newDiffuse.g, (float)newDiffuse.b, (float)newDiffuse.a};
		for (float channel : buffer) {
			if (channel < 0.0f)
				channel = 0.0f;
			else if (channel > 1.0f)
				channel = 1.0f;
		}
		this.diffuse = FloatBuffer.wrap(buffer);
	}
	
	public Color getDiffuse () {
		return new Color (this.diffuse.array()[0], this.diffuse.array()[1], this.diffuse.array()[2], this.diffuse.array()[3]);
	}
	
	public void setAmbient (Color newAmbient) {
		float[] buffer = {(float)newAmbient.r, (float)newAmbient.g, (float)newAmbient.b, (float)newAmbient.a};
		for (float channel : buffer) {
			if (channel < 0.0f)
				channel = 0.0f;
			else if (channel > 1.0f)
				channel = 1.0f;
		}
		this.ambient = FloatBuffer.wrap(buffer);
	}
	
	public Color getAmbient () {
		return new Color (this.ambient.array()[0], this.ambient.array()[1], this.ambient.array()[2], this.ambient.array()[3]);
	}
	
	public void setPosition (Point newPosition) {
		this.position = FloatBuffer.wrap(new float[] {(float)newPosition.x, (float)newPosition.y, (float)newPosition.z, 1.0f});
	}
	
	public Point getPosition () {
		return new Point (this.position.array()[0], this.position.array()[1], this.position.array()[2]);
	}
	
	public void setDirection (Vector newDirection) {
		this.direction = FloatBuffer.wrap(new float[] {(float)newDirection.x, (float)newDirection.y, (float)newDirection.z, 1.0f});
	}
	
	public Vector getDirection () {
		return new Vector (this.direction.array()[0], this.direction.array()[1], this.direction.array()[2]);
	}
	
	public void setSpot (double newSpotCutoff, double newSpotExponent) {
		this.spotCutoff = (float)newSpotCutoff;
		this.spotExponent = (float)newSpotExponent;
	}
	
	public double getSpotCutoff () {
		return this.spotCutoff;
	}
	
	public double getSpotExponent () {
		return this.spotExponent;
	}
	
	public void setAttenuation (double constant, double linear, double quadratic) {
		this.constantAttenuation = (float)constant;
		this.linearAttenuation = (float)linear;
		this.quadraticAttenuation = (float)quadratic;
	}
	
	public double getConstantAttenuation () {
		return this.constantAttenuation;
	}
	
	public double getLinearAttenuation () {
		return this.linearAttenuation;
	}
	
	public double getQuadraticAttenuation () {
		return this.quadraticAttenuation;
	}
	
	public void set(int lightNumber) {
		GL11.glLight(lightNumber, GL11.GL_AMBIENT, this.ambient);
		GL11.glLight(lightNumber, GL11.GL_DIFFUSE, this.diffuse);
		GL11.glLight(lightNumber, GL11.GL_SPECULAR, this.specular);
		GL11.glLight(lightNumber, GL11.GL_POSITION, this.position);
		GL11.glLight(lightNumber, GL11.GL_SPOT_DIRECTION, this.direction);
		GL11.glLightf(lightNumber, GL11.GL_SPOT_CUTOFF, this.spotCutoff);
		GL11.glLightf(lightNumber, GL11.GL_SPOT_EXPONENT, this.spotExponent);
		GL11.glLightf(lightNumber, GL11.GL_CONSTANT_ATTENUATION, this.constantAttenuation);
		GL11.glLightf(lightNumber, GL11.GL_LINEAR_ATTENUATION, this.linearAttenuation);
		GL11.glLightf(lightNumber, GL11.GL_QUADRATIC_ATTENUATION, this.quadraticAttenuation);
	}	
}
