package marten.util;

public class Dimension {
    public float width = 0;
    public float height = 0;
    public float depth = 0;

    public Dimension(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public Dimension(float width, float height, float depth) {
        this(width, height);
        this.depth = depth;
    }
}
