package marten.age.text;

public class BitmapFont {
    private int texture = -1;
    // font list start
    private int base = -1;
    private double size;

    public BitmapFont(int base, int texture, double size) {
        this.base = base;
        this.texture = texture;
        this.size = size;
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
}
