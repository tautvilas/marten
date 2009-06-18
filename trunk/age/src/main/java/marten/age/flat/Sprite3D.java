package marten.age.flat;

import marten.age.BasicSceneGraphBranch;
import marten.age.geometry.SimpleModel;
import marten.age.geometry.primitives.Rectangle;
import marten.age.texture.Texture;
import marten.age.transform.TranslationGroup;
import marten.util.Point;

public class Sprite3D extends BasicSceneGraphBranch {
    private SimpleModel model;
    private TranslationGroup tg = new TranslationGroup();

    public Sprite3D(Texture texture) {
        model = new SimpleModel(new Rectangle(texture.getDimension()));
        model.getAppearance().setTexture(texture);
        tg.addChild(model);
        this.addChild(tg);
    }

    public Sprite3D(Texture texture, Point position) {
        this(texture);
        setPosition(position);
    }

    public Point getPosition() {
        return tg.getCoordinates();
    }

    public void setPosition(Point position) {
        tg.setCoordinates(position);
    }
}
