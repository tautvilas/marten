package marten.age.graphics.flat;

import marten.age.graphics.BasicSceneGraphBranch;
import marten.age.graphics.appearance.Color;
import marten.age.graphics.geometry.SimpleModel;
import marten.age.graphics.geometry.primitives.Rectangle;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.texture.Texture;
import marten.age.graphics.texture.TextureLoader;
import marten.age.graphics.transform.TranslationGroup;

public class TexturedSprite extends BasicSceneGraphBranch {
    private SimpleModel model;
    private TranslationGroup tg = new TranslationGroup();

    public TexturedSprite(ImageData image) {
        this(TextureLoader.loadTexture(image));
    }

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
