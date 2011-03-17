package marten.age.widget;

import marten.age.control.KeyboardListener;
import marten.age.graphics.BasicSceneGraphBranch;
import marten.age.graphics.SceneGraphChild;
import marten.age.graphics.flat.sprite.TextureSprite;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.layout.BoxedObject;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.transform.TranslationGroup;

import org.lwjgl.input.Keyboard;

public class AgeField extends BasicSceneGraphBranch<SceneGraphChild> implements Widget, KeyboardListener, BoxedObject {

    private BitmapString input;
    private TextureSprite face;
    private TranslationGroup tg = new TranslationGroup();
    private BitmapFont font;
    private TextureSprite cursor;
    private int blinkInterval = 20;
    private int blinkCounter = 20;

    public AgeField(ImageData face, ImageData cursor, BitmapFont font) {
        this.face = new TextureSprite(face);
        this.cursor = new TextureSprite(cursor);
        this.cursor.setPosition(new Point(3, 0));
        this.font = font;
        this.tg.addChild(this.face);
        this.input = new BitmapString(this.font, "");
        this.tg.addChild(this.input);
        this.tg.addChild(this.cursor);
        this.addChild(tg);
    }

    public void setValue(String value) {
        this.input.setContent(value);
        cursor.setPosition(new Point(3 + this.input.getDimension().width, 0));
    }

    public String getValue() {
        return this.input.getContent();
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
        cursor.setPosition(new Point(3 + this.input.getDimension().width, 0));
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
        return tg.getPosition();
    }

    @Override
    public void setPosition(Point position) {
        tg.setPosition(position);
    }

    @Override
    public void activateChildren() {
        super.activateChildren();
        blinkCounter--;
        if (blinkCounter == 0) {
            if (tg.hasChild(cursor)) {
                tg.removeChild(cursor);
            } else {
                tg.addChild(cursor);
            }
            blinkCounter = blinkInterval;
        }
    }
}
