package marten.age.graphics.flat;

import marten.age.graphics.layout.BoxedObject;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;

public class HitTest {
    public static boolean testHit(Object o1, Object o2) {
        if ((o1 instanceof Point && o2 instanceof BoxedObject)
                || o1 instanceof BoxedObject && o2 instanceof Point) {
            Point point;
            BoxedObject sprite;
            if (o1 instanceof Point) {
                point = (Point)o1;
                sprite = (BoxedObject)o2;
            } else {
                point = (Point)o2;
                sprite = (BoxedObject)o1;
            }
            double dx = point.x - sprite.getPosition().x;
            double dy = point.y - sprite.getPosition().y;
            Dimension d = sprite.getDimension();
            if ((dx >= 0 && dx <= d.width) && (dy >= 0 && dy <= d.height)) {
                return true;
            }
            return false;
        }
        return false;
    }
}
