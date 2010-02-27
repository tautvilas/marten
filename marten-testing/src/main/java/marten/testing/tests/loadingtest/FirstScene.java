package marten.testing.tests.loadingtest;

import java.awt.Font;

import marten.age.core.AgeScene;
import marten.age.event.AgeEvent;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.flat.Sprite;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.image.ImageLoader;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import marten.age.graphics.util.Point;
import marten.age.io.SimpleLoader;

public class FirstScene extends AgeScene {

    private Flatland flatland = null;
    private SecondScene scene2 = new SecondScene();
    private SimpleLoader loader = new SimpleLoader(scene2);
    private boolean loading = false;

    public FirstScene() {
        this.flatland = new Flatland();

        // TODO:zv:develop a decent image loading/caching system
        ImageData buttonImage = ImageLoader
                .loadImage("data/testloading/button.png");
        Sprite button = new Sprite(buttonImage);
        BitmapFont font = FontCache.getFont(new Font("Courier New",
                Font.BOLD, 20));
        BitmapString info = new BitmapString(font, "Click 'load' to load.");
        this.flatland.addSprite(button, new Point(200, 200));
        this.flatland.addText(info, new Point(200, 400));
    }

    @Override
    public void compute() {
        if (!loading) {
            loader.load();
            loading = true;
        }

        System.out.println(loader.getStatus() + loader.getPercentage());
        if (loader.loadingFinished()) {
            this.fireEvent(new AgeEvent("SCENE SWITCH", scene2));
        }
    }

    @Override
    public void render() {
        this.flatland.render();
    }
}
