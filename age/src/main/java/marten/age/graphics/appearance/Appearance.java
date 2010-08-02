package marten.age.graphics.appearance;

import java.nio.FloatBuffer;

import marten.age.graphics.texture.Texture;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Appearance {
    private FloatBuffer specular = null;
    private FloatBuffer diffuse = null;
    private FloatBuffer ambient = null;
    private FloatBuffer emission = null;
    private Color color = null;
    private float shininess = -1f;
    private Texture texture = null;

    public Appearance() {
    }

    public Appearance(Color color) {
        this();
        this.color = color;
    }

    public Appearance(Texture texture) {
        this();
        this.texture = texture;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public Appearance(float newShininess, Color newSpecular, Color newDiffuse,
            Color newAmbient, Color newEmission, Texture texture) {
        this(texture);
        this.setAmbient(newAmbient);
        this.setDiffuse(newDiffuse);
        this.setEmission(newEmission);
        this.setShininess(newShininess);
        this.setSpecular(newSpecular);
    }

    public void setSpecular(Color newSpecular) {
        float[] buffer = { (float) newSpecular.r, (float) newSpecular.g,
                (float) newSpecular.b, (float) newSpecular.a };
        for (float channel : buffer) {
            if (channel < 0.0f)
                channel = 0.0f;
            else if (channel > 1.0f)
                channel = 1.0f;
        }
        this.specular = BufferUtils.createFloatBuffer(4);
        this.specular.put(buffer);
        this.specular.rewind();
    }

    public Color getSpecular() {
        return new Color(this.specular.array()[0], this.specular.array()[1],
                this.specular.array()[2], this.specular.array()[3]);
    }

    public void setDiffuse(Color newDiffuse) {
        float[] buffer = { (float) newDiffuse.r, (float) newDiffuse.g,
                (float) newDiffuse.b, (float) newDiffuse.a };
        for (float channel : buffer) {
            if (channel < 0.0f)
                channel = 0.0f;
            else if (channel > 1.0f)
                channel = 1.0f;
        }
        this.diffuse = BufferUtils.createFloatBuffer(4);
        this.diffuse.put(buffer);
        this.diffuse.rewind();
    }

    public void setColor(Color newColor) {
        this.color = newColor;
    }

    public Color getColor() {
        return color;
    }

    public Color getDiffuse() {
        return new Color(this.diffuse.array()[0], this.diffuse.array()[1],
                this.diffuse.array()[2], this.diffuse.array()[3]);
    }

    public void setAmbient(Color newAmbient) {
        float[] buffer = { (float) newAmbient.r, (float) newAmbient.g,
                (float) newAmbient.b, (float) newAmbient.a };
        for (float channel : buffer) {
            if (channel < 0.0f)
                channel = 0.0f;
            else if (channel > 1.0f)
                channel = 1.0f;
        }
        this.ambient = BufferUtils.createFloatBuffer(4);
        this.ambient.put(buffer);
        this.ambient.rewind();
    }

    public Color getAmbient() {
        return new Color(this.ambient.array()[0], this.ambient.array()[1],
                this.ambient.array()[2], this.ambient.array()[3]);
    }

    public void setEmission(Color newEmission) {
        float[] buffer = { (float) newEmission.r, (float) newEmission.g,
                (float) newEmission.b, (float) newEmission.a };
        for (float channel : buffer) {
            if (channel < 0.0f)
                channel = 0.0f;
            else if (channel > 1.0f)
                channel = 1.0f;
        }
        this.emission = BufferUtils.createFloatBuffer(4);
        this.emission.put(buffer);
        this.emission.rewind();
    }

    public Color getEmission() {
        return new Color(this.emission.array()[0], this.emission.array()[1],
                this.emission.array()[2], this.emission.array()[3]);
    }

    public void setShininess(double newShininess) {
        if (newShininess < 0.0)
            newShininess = 0.0;
        else if (newShininess > 128.0)
            newShininess = 128.0;
        this.shininess = (float) newShininess;
    }

    public double getShininess() {
        return this.shininess;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void set() {
        if (this.color != null) {
            GL11.glColor3d(color.r, color.g, color.b);
        }
        if (this.shininess != -1f) {
            GL11.glMaterialf(GL11.GL_FRONT_AND_BACK, GL11.GL_SHININESS,
                    this.shininess);
        }
        if (this.ambient != null) {
            GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT,
                    this.ambient);
        }
        if (this.diffuse != null) {
            GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_DIFFUSE,
                    this.diffuse);
        }
        if (this.emission != null) {
            GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION,
                    this.emission);
        }
        if (this.specular != null) {
            GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_SPECULAR,
                    this.specular);
        }
        if (this.texture != null) {
            // if (texture.getTextureId() != -1) {
            if (!GL11.glIsEnabled(GL11.GL_TEXTURE_2D)) {
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureId());
        } else if (GL11.glIsEnabled(GL11.GL_TEXTURE_2D)) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
        }
    }
}
