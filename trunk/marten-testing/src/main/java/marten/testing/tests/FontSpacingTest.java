package marten.testing.tests;

import java.awt.Font;

import marten.age.core.AgeApp;
import marten.age.core.AgeScene;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.flat.sprite.TextureSprite;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;

public class FontSpacingTest extends AgeApp {

    private class MainScene extends AgeScene {

        private Flatland flatland = new Flatland();

        public MainScene() {
            BitmapFont font = FontCache.getFont(new Font("Courier New",
                    Font.PLAIN, 14));
            BitmapString string = new BitmapString(
                    font,
                    "The quick brown fox jumps over the lazy dog\nThe quick brown fox jumps over the lazy dog\nWWWWWW");
            BitmapString string2 = new BitmapString(
                    font,
                    "Ground zero qyp");
            string.setPosition(new Point(0, 500));
            TextureSprite sprite = new TextureSprite(font.getTexture());
            sprite.setPosition(new Point(0, 50));
            flatland.addChild(sprite);
            flatland.addChild(string);
            flatland.addChild(string2);
            flatland.compile();
        }

        @Override
        public void compute() {
        }

        @Override
        public void render() {
            flatland.render();
        }

    }

    @Override
    public void configure() {
        this.setActiveScene(new MainScene());
    }

}
