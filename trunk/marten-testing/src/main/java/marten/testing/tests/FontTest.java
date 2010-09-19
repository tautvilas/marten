package marten.testing.tests;

import java.awt.Font;

import marten.age.core.AgeApp;
import marten.age.core.AgeScene;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import marten.age.widget.obsolete.FpsCounter;

public class FontTest extends AgeApp {

    private class MainScene extends AgeScene {

        private Flatland flatland = new Flatland();

        public MainScene() {
            BitmapFont font = FontCache.getFont(new Font("Courier New",
                    Font.PLAIN, 20));
            BitmapString string = new BitmapString(
                    font,
                    "The quick brown fox jumps over the lazy dog\nThe quick brown fox jumps over the lazy dog");
            string.setPosition(new Point(0, 550));
            flatland.addChild(string);

            font = FontCache.getFont(new Font("Courier New",
                    Font.BOLD, 20));
            string = new BitmapString(
                    font,
                    "The quick brown fox jumps over the lazy dog\nThe quick brown fox jumps over the lazy dog");
            string.setPosition(new Point(0, 500));
            flatland.addChild(string);

            font = FontCache.getFont(new Font("Courier New",
                    Font.ITALIC, 20));
            string = new BitmapString(
                    font,
                    "The quick brown fox jumps over the lazy dog\nThe quick brown fox jumps over the lazy dog");
            string.setPosition(new Point(0, 450));
            flatland.addChild(string);

            font = FontCache.getFont(new Font("Courier New",
                    Font.PLAIN, 10));
            string = new BitmapString(
                    font,
                    "The quick brown fox jumps over the lazy dog\nThe quick brown fox jumps over the lazy dog");
            string.setPosition(new Point(0, 400));
            flatland.addChild(string);

            font = FontCache.getFont(new Font("Courier New",
                    Font.PLAIN, 12));
            string = new BitmapString(
                    font,
                    "The quick brown fox jumps over the lazy dog\nThe quick brown fox jumps over the lazy dog");
            string.setPosition(new Point(0, 370));
            flatland.addChild(string);

            font = FontCache.getFont(new Font("Courier New",
                    Font.PLAIN, 14));
            string = new BitmapString(
                    font,
                    "The quick brown fox jumps over the lazy dog\nThe quick brown fox jumps over the lazy dog");
            string.setPosition(new Point(0, 320));
            flatland.addChild(string);

            font = FontCache.getFont(new Font("Courier New",
                    Font.PLAIN, 16));
            string = new BitmapString(
                    font,
                    "The quick brown fox jumps over the lazy dog\nThe quick brown fox jumps over the lazy dog");
            string.setPosition(new Point(0, 280));
            flatland.addChild(string);

            font = FontCache.getFont(new Font("Courier New",
                    Font.PLAIN, 50));
            string = new BitmapString(
                    font,
                    "The quick brown fox jumps over the lazy dog\nThe quick brown fox jumps over the lazy dog");
            string.setPosition(new Point(0, 200));
            flatland.addChild(string);

            font = FontCache.getFont(new Font("Times New Roman",
                    Font.PLAIN, 30));
            string = new BitmapString(
                    font,
                    "The quick brown fox jumps over the lazy dog\nThe quick brown fox jumps over the lazy dog");
            string.setPosition(new Point(0, 100));
            flatland.addChild(string);
            flatland.addChild(new FpsCounter());
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
