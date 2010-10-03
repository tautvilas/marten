package marten.age.widget;

import marten.age.control.MouseListener;
import marten.age.graphics.appearance.Color;
import marten.age.graphics.flat.HitTest;
import marten.age.graphics.flat.SimpleLayout;
import marten.age.graphics.flat.sprite.TextureSprite;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;

import org.apache.log4j.Logger;

public class Button extends SimpleLayout implements Widget, MouseListener {
    @SuppressWarnings("unused")
    private static org.apache.log4j.Logger log = Logger.getLogger(Button.class);

    private boolean pressed = false;
    private boolean mouseOver = false;
    private Action action;
    private TextureSprite face;
    private BitmapString label;
    private BitmapFont font;

    public Button(ImageData data) {
        super(data.getDimension());
        this.face = new TextureSprite(data);
        this.addChild(face);
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }

    public void setLabel(String label) {
        this.label = new BitmapString(this.font);
        this.label.setContent(label);
        this.removeChild(this.label);
        this.center(this.label);
    }

    @Override
    public void mouseDown(Point coords) {
        if (HitTest.testHit(coords, this)) {
            this.pressed = true;
            this.setPosition(new Point(this.getPosition().x + 3, this
                    .getPosition().y - 3));
        }
    }

    @Override
    public void mouseMove(Point coords) {
        if (HitTest.testHit(coords, this) && !this.mouseOver) {
            this.mouseOver = true;
            if (this.label != null) {
                this.label.setColor(new Color(0, 1.0, 0));
            }
        } else if (!HitTest.testHit(coords, this) && this.mouseOver) {
            if (this.label != null) {
                this.label.setColor(new Color(1, 1, 1));
            }
            this.mouseOver = false;
        }
    }

    @Override
    public void mouseUp(Point coords) {
        if (this.pressed) {
            if (HitTest.testHit(coords, this) && this.action != null) {
                this.action.perform();
            }
            this.setPosition(new Point(this.getPosition().x - 3, this
                    .getPosition().y + 3));
        }
        this.pressed = false;
    }

    @Override
    public void mouseWheelRoll(int delta) {
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public Dimension getDimension() {
        return this.face.getDimension();
    }
}
