package marten.testing.tests.maptest;

import marten.age.core.AgeScene;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.flat.Sprite;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.image.ImageLoader;
import marten.age.graphics.util.Point;

public class Map extends AgeScene {

    private Flatland flatland = null;

    public Map() {
        this.flatland = new Flatland();
        ImageData tileImage = ImageLoader.loadImage("data/map_test/desert.png");

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                flatland.addSprite(new Sprite(tileImage, new Point(i
                        * (65 + 32), j * 65)));
                flatland.addSprite(new Sprite(tileImage), new Point(48 + i
                        * (65 + 32), 32 + j * 65));
            }
        }
    }

    @Override
    public void compute() {
    }

    @Override
    public void render() {
        flatland.render();
    }

}
