package marten.age.graphics.flat.sprite;

import marten.age.graphics.BasicSceneGraphBranch;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;

public abstract class Sprite extends BasicSceneGraphBranch {
    public abstract void setPosition(Point position);

    public abstract Point getPosition();

    public abstract Dimension getDimension();
}
