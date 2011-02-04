package marten.age.graphics.layout;

import marten.age.graphics.SceneGraphChild;
import marten.age.graphics.primitives.Dimension;

public interface BoxedObject extends Placeable, SceneGraphChild {
    public Dimension getDimension();
}
