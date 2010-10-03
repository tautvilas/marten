package marten.age.graphics.flat.sprite;

import marten.age.graphics.SceneGraphChild;
import marten.age.graphics.primitives.Dimension;

public interface Sprite extends Placeable, SceneGraphChild {
    public Dimension getDimension();
}
