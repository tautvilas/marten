package marten.age.widget;

import java.awt.Font;

import marten.age.control.KeyboardListener;
import marten.age.graphics.flat.sprite.Sprite;
import marten.age.graphics.flat.sprite.TextureSprite;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import marten.age.graphics.transform.TranslationGroup;

import org.lwjgl.input.Keyboard;

public class AgeField extends Sprite implements Widget, KeyboardListener {

    private BitmapString input;
    private TextureSprite face;
    private TranslationGroup tg = new TranslationGroup();
    private BitmapFont font;

    public AgeField(ImageData face, Font font) {
        this.face = new TextureSprite(face);
        this.font = FontCache.getFont(font);
        this.tg.addChild(this.face);
        this.input = new BitmapString(this.font, "");
        this.tg.addChild(this.input);
        this.addChild(tg);
    }

    @Override
    public void keyDown(int key, char character) {
        String inputString = this.input.getContent();
        if (character >= 32 && character <= 126) {
            inputString += character;
        } else if (key == Keyboard.KEY_BACK) {
            if (inputString.length() != 0) {
                inputString = inputString
                        .substring(0, inputString.length() - 1);
            }
        }
        input.setContent(inputString);
    }

    @Override
    public void keyUp(int key, char character) {
    }

    @Override
    public Dimension getDimension() {
        return this.face.getDimension();
    }

    @Override
    public Point getPosition() {
        return tg.getCoordinates();
    }

    @Override
    public void setPosition(Point position) {
        tg.setCoordinates(position);
    }
}
