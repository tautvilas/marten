package marten.age.graphics.text;

public class BitmapFont {
    private int texture = -1;
    // font list start
    private int base = -1;
    private double size;
    private int charWidth;

    public BitmapFont(int base, int texture, double size, int charWidth) {
        this.base = base;
        this.texture = texture;
        this.size = size;
        this.charWidth = charWidth;
    }

    public int getBase() {
        return this.base;
    }

    public int getTexture() {
        return this.texture;
    }

    public double getSize() {
        return this.size;
    }

    public int getCharWidth() {
        return this.charWidth;
    }
}
