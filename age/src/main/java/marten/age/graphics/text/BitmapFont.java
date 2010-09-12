package marten.age.graphics.text;

import java.awt.FontMetrics;

import marten.age.graphics.texture.Texture;

public class BitmapFont {
    private Texture texture = null;
    // font list start
    private int base = -1;
    private double size;
    private int charWidth;
    private FontMetrics metrics;

    public BitmapFont(int base, Texture texture, double size, int charWidth,
            FontMetrics metrics) {
        this.base = base;
        this.texture = texture;
        this.size = size;
        this.charWidth = charWidth;
        this.metrics = metrics;
    }

    public int getBase() {
        return this.base;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public FontMetrics getMetrics() {
        return this.metrics;
    }

    public double getSize() {
        return this.size;
    }

    public int getCharWidth() {
        return this.charWidth;
    }
}
