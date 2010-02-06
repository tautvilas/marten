package marten.testing.tests.loadingtest;

import marten.age.core.AgeScene;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.flat.Sprite;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.image.ImageLoader;
import marten.age.graphics.util.Point;
import marten.age.io.Loadable;
import marten.age.io.SimpleLoader;

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
    public void load(SimpleLoader loader) {
        loader.setEstimatable(true);
        loader.setStatus("Starting up...");
        long tag = System.currentTimeMillis();
        int percentage = 0;
        while (percentage != 100) {
            long now = System.currentTimeMillis();
            if (now - 100 > tag) {
                tag = now;
                percentage += 10;
                loader.setPercentage(percentage);
                loader.setStatus("[" + percentage + "]");
            }
        }
        ImageData buttonImage = ImageLoader
                .loadImage("data/textures/sprite.png");
        Sprite button = new Sprite(buttonImage);
        flatland.addSprite(button, new Point(200, 200));
    }

}
