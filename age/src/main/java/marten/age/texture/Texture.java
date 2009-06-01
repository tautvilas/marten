package marten.age.texture;

import marten.util.Dimension;

public class Texture {
    private Dimension dimension;
    private int textureId = -1;

    public Texture(int textureId, Dimension dimension) {
        this.textureId = textureId;
    }

    public int getTextureId() {
        return this.textureId;
    }

    public Dimension getDimension() {
        return dimension;
    }
}
