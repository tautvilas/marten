package marten.age.graphics.texture;

import marten.age.graphics.primitives.Dimension;

public class Texture {
    private Dimension dimension;
    private int textureId = -1;

    public Texture(int textureId, Dimension dimension) {
        this.textureId = textureId;
        this.dimension = dimension;
    }

    public int getTextureId() {
        return this.textureId;
    }

    public Dimension getDimension() {
        return dimension;
    }
}
