package marten.age.graphics.flat.sprite;

import marten.age.graphics.BasicSceneGraphBranch;
import marten.age.graphics.SceneGraphChild;
import marten.age.graphics.appearance.Appearance;
import marten.age.graphics.appearance.Color;
import marten.age.graphics.geometry.primitives.Rectangle;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.layout.BoxedObject;
import marten.age.graphics.model.SimpleModel;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.texture.Texture;
import marten.age.graphics.texture.TextureLoader;
import marten.age.graphics.transform.TranslationGroup;

public class TextureSprite extends BasicSceneGraphBranch<SceneGraphChild> implements BoxedObject {
    private SimpleModel model;
    private TranslationGroup tg = new TranslationGroup();
    private Dimension dimension;

    public TextureSprite(ImageData image) {
        this(TextureLoader.loadTexture(image));
    }

    public TextureSprite(ImageData image, Point position) {
        this(TextureLoader.loadTexture(image));
        this.setPosition(position);
    }

    public TextureSprite(ImageData image, Dimension dimension) {
        this(TextureLoader.loadTexture(image), dimension);
    }

    public TextureSprite(Texture texture) {
        this(texture, texture.getDimension());
    }

    public TextureSprite(Texture texture, Dimension dimension) {
        this.dimension = dimension;
        model = new SimpleModel(new Rectangle(dimension));
        // glTexEnv(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_BLEND)
        // gl<...same shit...>, GL_REPLACE) for no background blending
        model.getAppearance().setColor(new Color(1.0, 1.0, 1.0));
        model.getAppearance().setTexture(texture);
        tg.addChild(model);
        this.addChild(tg);
    }

    public void setColor(Color color) {
        model.getAppearance().setColor(color);
    }

    public Appearance getAppearance() {
        return model.getAppearance();
    }

    public TextureSprite(Texture texture, Point position) {
        this(texture);
        setPosition(position);
    }

    @Override
    public Dimension getDimension() {
        return this.dimension;
    }

    @Override
    public Point getPosition() {
        return tg.getPosition();
    }

    @Override
    public void setPosition(Point point) {
        tg.setPosition(point);
    }
}
