package marten.testing.tests.loadingtest;

import marten.age.core.AgeScene;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.flat.Sprite;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.image.ImageLoader;
import marten.age.graphics.primitives.Point;
import marten.age.io.Loadable;
import marten.age.io.LoadingState;

public class SecondScene extends AgeScene implements Loadable {

    private Flatland flatland;

    public SecondScene() {
        flatland = new Flatland();
    }

    @Override
    public void compute() {
    }

    @Override
    public void render() {
        flatland.render();
    }

    @Override
    public void load(LoadingState state) {
        state.status = "Starting up...";
        long tag = System.currentTimeMillis();
        int percentage = 0;
        while (percentage != 100) {
            long now = System.currentTimeMillis();
            if (now - 100 > tag) {
                tag = now;
                percentage += 10;
                state.percentage = percentage;
                state.status = "[" + percentage + "]";
            }
        }
        ImageData buttonImage = ImageLoader
                .loadImage("data/textures/sprite.png");
        Sprite button = new Sprite(buttonImage, new Point(200, 200));
        flatland.addChild(button);
    }

}
