package marten.age.graphics.layout;

import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.transform.TranslationGroup;

public class SimpleLayout extends TranslationGroup implements BoxedObject {
    private Dimension dimension;

    public SimpleLayout(Dimension dimension) {
        this.dimension = dimension;
    }

    public Dimension getDimension() {
        return this.dimension;
    }

    public void addToLeft(BoxedObject sprite) {
        sprite.setPosition(new Point(0, 0));
        this.addChild(sprite);
    }

    public void addToRight(BoxedObject sprite) {
        sprite.setPosition(new Point(this.dimension.width
                - sprite.getDimension().width, 0));
        this.addChild(sprite);
    }

    public void center(BoxedObject sprite) {
        Dimension d1 = sprite.getDimension();
        Point p2 = this.getPosition();
        Dimension d2 = this.getDimension();

        sprite.setPosition(new Point(p2.x + d2.width / 2 - d1.width / 2, p2.y
                + d2.height / 2 - d1.height / 2));
        this.addChild(sprite);
    }

    public void centerHorizontally(BoxedObject sprite, int y) {
        Dimension d1 = sprite.getDimension();
        Point p2 = this.getPosition();
        Dimension d2 = this.getDimension();

        sprite.setPosition(new Point(p2.x + d2.width / 2 - d1.width / 2, y));
        this.addChild(sprite);
    }
}
