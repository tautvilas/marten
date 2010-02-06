package marten.testing.tests.loadingtest;

import marten.age.core.AgeScene;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.flat.Sprite;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.image.ImageLoader;
import marten.age.graphics.util.Point;

public class SecondScene extends AgeScene implements Loadable {

    private Flatland flatland;

    @Override
    public void init() {
        ImageData buttonImage = ImageLoader
                .loadImage("data/textures/sprite.png");
        flatland = new Flatland();
        Sprite button = new Sprite(buttonImage);
        flatland.addSprite(button, new Point(200, 200));
    }

    @Override
    public void compute() {
    }

    @Override
    public void render() {
        flatland.render();
    }

}
