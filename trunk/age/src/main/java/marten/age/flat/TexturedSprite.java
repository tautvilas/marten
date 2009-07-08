package marten.age.flat;

import marten.age.BasicSceneGraphBranch;
import marten.age.geometry.SimpleModel;
import marten.age.geometry.primitives.Rectangle;
import marten.age.texture.Texture;
import marten.age.transform.TranslationGroup;
import marten.age.util.Color;
import marten.util.Point;

public class TexturedSprite extends BasicSceneGraphBranch {
    private SimpleModel model;
    private TranslationGroup tg = new TranslationGroup();

    public TexturedSprite(Texture texture) {
        model = new SimpleModel(new Rectangle(texture.getDimension()));
        // glTexEnv(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_BLEND)
        // gl<...same shit...>, GL_REPLACE) for no background blending
        model.getAppearance().setColor(new Color(1.0, 1.0, 1.0));
        model.getAppearance().setTexture(texture);
        tg.addChild(model);
        this.addChild(tg);
    }

    public TexturedSprite(Texture texture, Point position) {
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
