package marten.testing.tests.loadingtest;

import java.awt.Font;

import marten.age.control.MouseController;
import marten.age.core.AgeScene;
import marten.age.event.AgeEvent;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.image.ImageLoader;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import marten.age.io.SimpleLoader;
import marten.age.widget.Action;
import marten.age.widget.Button;

public class FirstScene extends AgeScene {

    private Flatland flatland = null;
    private BitmapString info;
    private SecondScene scene2 = new SecondScene();
    private SimpleLoader loader = new SimpleLoader(scene2);
    private boolean loading = false;
    private Button button;

    public FirstScene() {
        this.flatland = new Flatland();

        // TODO:zv:develop a decent image loading/caching system
        ImageData buttonImage = ImageLoader
                .loadImage("data/testloading/button.png");
        BitmapFont font = FontCache.getFont(new Font("Courier New", Font.BOLD,
                20));
        info = new BitmapString(font, "Click 'load' to load.");
        this.flatland.addText(info, new Point(200, 400));

        button = new Button(buttonImage);
        button.setPosition(new Point(200, 200));
        button.setAction(new Action() {
            @Override
            public void perform() {
                if (!loading) {
                    loader.load();
                    loading = true;
                    info.setContent("Loading...................");
                    flatland.removeChild(button);
                }
            }
        });
        this.flatland.addChild(button);
        this.addController(new MouseController());
        this.registerControllable(button);
    }

    @Override
    public void compute() {
        if (loading) {
            System.out.println(loader.getStatus() + loader.getPercentage());
        }
        if (loader.loadingFinished()) {
            fireEvent(new AgeEvent("SCENE SWITCH", scene2));
        }
    }

    @Override
    public void render() {
        this.flatland.render();
    }
}
