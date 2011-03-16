package marten.age.graphics.layout;

import marten.age.graphics.BasicSceneGraphChild;
import marten.age.graphics.SceneGraphChild;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.transform.TranslationGroup;

public class LayoutContainer extends BasicSceneGraphChild implements BoxedObject {

    private TranslationGroup tg = new TranslationGroup();

    @Override
    public Dimension getDimension() {
        int width = 0;
        int height = 0;
        for (SceneGraphChild child : this.tg.getBranches()) {
            BoxedObject object = (BoxedObject)child;
            if (object.getPosition().x + object.getDimension().width > width) {
                width = (int)object.getPosition().x + (int)object.getDimension().width;
            }
            if (object.getPosition().y + object.getDimension().height > height) {
                height = (int)object.getPosition().y + (int)object.getDimension().height;
            }
        }
        return new Dimension(width, height);
    }

    public void addBoxedObject(BoxedObject object) {
        this.tg.addChild(object);
    }

    @Override
    public Point getPosition() {
        return tg.getPosition();
    }

    @Override
    public void setPosition(Point position) {
        tg.setPosition(position);
    }

    @Override
    public void render() {
        this.tg.render();
    }

}
