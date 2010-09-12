package marten.age.graphics.flat.sprite;

import marten.age.graphics.BasicSceneGraphBranch;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;

public abstract class Sprite extends BasicSceneGraphBranch {
    public abstract void setPosition(Point position);

    public abstract Point getPosition();

    public abstract Dimension getDimension();

    public void centerIn(Sprite sprite2) {
        Dimension d1 = this.getDimension();
        Point p2 = sprite2.getPosition();
        Dimension d2 = sprite2.getDimension();

        this.setPosition(new Point(p2.x + d2.width / 2 - d1.width / 2, p2.y
                + d2.height / 2 - d1.height / 2));
    }

    public void centerHorizontallyIn(Sprite sprite2, int y) {
        Dimension d1 = this.getDimension();
        Point p2 = sprite2.getPosition();
        Dimension d2 = sprite2.getDimension();

        this.setPosition(new Point(p2.x + d2.width / 2 - d1.width / 2, y));
    }

    public boolean testHit(Point coords) {
        double dx = coords.x - this.getPosition().x;
        double dy = coords.y - this.getPosition().y;
        Dimension d = this.getDimension();
        if ((dx >= 0 && dx <= d.width)
                && (dy >= 0 && dy <= d.height)) {
            return true;
        }
        return false;
    }
}
