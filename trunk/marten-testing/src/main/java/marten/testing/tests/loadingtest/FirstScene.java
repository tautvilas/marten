package marten.testing.tests.loadingtest;

import marten.age.core.AgeScene;
import marten.age.event.AgeEvent;
import marten.age.flat.Flatland;
import marten.age.flat.Sprite;
import marten.age.image.ImageData;
import marten.age.image.ImageLoader;
import marten.util.Point;

public class FirstScene extends AgeScene {

    private Flatland flatland = null;

    @Override
    public void init() {
        // TODO:zv:develop a decent image loading/caching system
        ImageData buttonImage = ImageLoader
                .loadImage("data/testloading/button.png");
        flatland = new Flatland();
        Sprite button = new Sprite(buttonImage);
        flatland.addSprite(button, new Point(200, 200));
    }

    @Override
    public void compute() {
        AgeScene scene2 = new SecondScene();
        this.fireEvent(new AgeEvent("SWITCH SCENE", scene2));
    }

    @Override
    public void render() {
        flatland.render();
    }

    @Override
    public void cleanup() {
    }

}
