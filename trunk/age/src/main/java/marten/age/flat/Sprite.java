package marten.age.flat;

import marten.age.BasicSceneGraphChild;
import marten.age.appearance.Appearance;
import marten.age.geometry.Geometry;
import marten.age.geometry.primitives.Rectangle;
import marten.age.texture.Texture;

public class Sprite extends BasicSceneGraphChild {
    private Appearance appearance = new Appearance();
    private Geometry geometry = null;

    public Sprite(Texture texture) {
        this.geometry = new Rectangle(texture.getDimension());
        appearance.setTexture(texture);
    }

    public void activate() {
        appearance.set();
        geometry.draw();
    }
}
