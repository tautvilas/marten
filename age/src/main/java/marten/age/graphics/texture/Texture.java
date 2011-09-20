package marten.age.graphics.texture;

import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.primitives.TextureCoords;

public class Texture {
    private Dimension dimension;
    private Dimension canvasDimension;
    private TextureCoords coords;
    private int textureId = -1;

    public Texture(int textureId, Dimension dimension) {
        this(textureId, dimension, dimension);
    }

    public Texture(int textureId, Point position, Dimension dimension,
            Dimension canvasDimension) {
        this.textureId = textureId;
        this.dimension = dimension;
        this.canvasDimension = canvasDimension;
        this.coords = new TextureCoords(new Point(position.x
                / canvasDimension.width, position.y / canvasDimension.height),
                new Dimension(dimension.width / canvasDimension.width,
                        dimension.height / canvasDimension.height));
    }

    public Texture(int textureId, Dimension dimension, Dimension canvasDimension) {
        this(textureId, new Point(), dimension, canvasDimension);
    }

    public Texture crop(Point position, Dimension dimension) {
        return new Texture(this.textureId, position, dimension,
                this.canvasDimension);
    }

    public int getTextureId() {
        return this.textureId;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public TextureCoords getCoords() {
        return this.coords;
    }
}
