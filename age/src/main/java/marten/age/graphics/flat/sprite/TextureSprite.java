package marten.age.graphics.flat.sprite;

import marten.age.graphics.appearance.Appearance;
import marten.age.graphics.appearance.Color;
import marten.age.graphics.geometry.primitives.Rectangle;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.model.SimpleModel;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.texture.Texture;
import marten.age.graphics.texture.TextureLoader;
import marten.age.graphics.transform.TranslationGroup;

public class TextureSprite extends Sprite {
    private SimpleModel model;
    private TranslationGroup tg = new TranslationGroup();
    private Texture texture;

    public TextureSprite(ImageData image) {
        this(TextureLoader.loadTexture(image));
    }

    public TextureSprite(ImageData image, Point position) {
        this(TextureLoader.loadTexture(image));
        setPosition(position);
    }

    public TextureSprite(Texture texture) {
        this.texture = texture;
        model = new SimpleModel(new Rectangle(texture.getDimension()));
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
        return texture.getDimension();
    }

    @Override
    public Point getPosition() {
        return tg.getCoordinates();
    }

    @Override
    public void setPosition(Point point) {
        tg.setCoordinates(point);
    }
}