package marten.age.graphics.primitives;

public class TextureCoords {
    private Point position = new Point(0, 0);
    private Dimension dimension = new Dimension(1, 1);

    public TextureCoords() {
        super();
    }

    public TextureCoords(Point position, Dimension dimension) {
        super();
        this.position = position;
        this.dimension = dimension;
    }

    public Point getPosition() {
        return position;
    }

    public Dimension getDimension() {
        return dimension;
    }
}
