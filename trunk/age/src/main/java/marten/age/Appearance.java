package marten.age;

import java.nio.FloatBuffer;

import marten.age.util.Color;

import org.lwjgl.opengl.GL11;



public class Appearance {
	private FloatBuffer specular = FloatBuffer.wrap(new float[] {0.0f, 0.0f, 0.0f, 1.0f});
	private FloatBuffer diffuse = FloatBuffer.wrap(new float[] {0.8f, 0.8f, 0.8f, 1.0f});
	private FloatBuffer ambient = FloatBuffer.wrap(new float[] {0.2f, 0.2f, 0.2f, 1.0f});
	private FloatBuffer emission = FloatBuffer.wrap(new float[] {0.0f, 0.0f, 0.0f, 1.0f});
	private Color color = new Color(0.0, 0.0, 0.0);
	private float shininess = 0.0f;
	private Texture texture;
	
	public Appearance() {}
	
	public Appearance(Texture texture) {
		this.texture = texture;
	}
	
	public Appearance (float newShininess, Color newSpecular, Color newDiffuse, Color newAmbient, Color newEmission, Texture texture) {
		this(texture);
		this.setAmbient(newAmbient);
		this.setDiffuse(newDiffuse);
		this.setEmission(newEmission);
		this.setShininess(newShininess);
		this.setSpecular(newSpecular);
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
	
	public void setColor (Color newColor) {
		this.color = newColor;
	}
	
	public Color getColor() {
		return color;
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
	
	public void setEmission (Color newEmission) {
		float[] buffer = {(float)newEmission.r, (float)newEmission.g, (float)newEmission.b, (float)newEmission.a};
		for (float channel : buffer) {
			if (channel < 0.0f)
				channel = 0.0f;
			else if (channel > 1.0f)
				channel = 1.0f;
		}
		this.emission = FloatBuffer.wrap(buffer);
	}
	
	public Color getEmission () {
		return new Color (this.emission.array()[0], this.emission.array()[1], this.emission.array()[2], this.emission.array()[3]);
	}
	
	public void setShininess (double newShininess) {
		if (newShininess < 0.0)
			newShininess = 0.0;
		else if (newShininess > 128.0)
			newShininess = 128.0;
		this.shininess = (float)newShininess;
	}
	
	public double getShininess () {
		return this.shininess;
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public void set() {
		GL11.glColor3d(color.r, color.g, color.b);
		GL11.glMaterialf(GL11.GL_FRONT_AND_BACK, GL11.GL_SHININESS, this.shininess);
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT, this.ambient);
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_DIFFUSE, this.diffuse);
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, this.emission);
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_SPECULAR, this.specular);
		if (texture != null && texture.getTextureId() != -1) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureId());
		} else {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
	}
}
