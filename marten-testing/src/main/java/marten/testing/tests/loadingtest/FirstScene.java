package marten.testing.tests.loadingtest;

import java.io.IOException;

import marten.age.core.AgeScene;
import marten.age.event.AgeEvent;
import marten.age.flat.Flatland;
import marten.age.flat.Sprite;
import marten.age.image.ImageData;
import marten.util.Point;

public class FirstScene implements AgeScene {

    private Flatland flatland = null;

    @Override
    public void init() {
        // TODO:zv:develop a decent image loading/caching system
        ImageData buttonImage;
        try {
            buttonImage = new ImageData("data/testloading/button.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        flatland = new Flatland();
        Sprite button = new Sprite(buttonImage);
        flatland.addSprite(button, new Point(100, 100));
    }

    @Override
    public void handle(AgeEvent e) {
    }

    @Override
    public void compute() {
    }

    @Override
    public void render() {
        flatland.activate();
    }

    @Override
    public void cleanup() {
    }

}
