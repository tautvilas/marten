package marten.age.graphics.primitives;

public final class Dimension {
    public float width = 0;
    public float height = 0;
    public float depth = 0;

    public Dimension() {
    }

    public Dimension(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public Dimension(float width, float height, float depth) {
        this(width, height);
        this.depth = depth;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Dimension) {
            Dimension d2 = (Dimension) o;
            if (this.height == d2.height && this.width == d2.width)
                return true;
            else
                return false;
        } else {
            return super.equals(o);
        }
    }
}
