package marten.age.widget;

import java.awt.Font;

import marten.age.control.MouseListener;
import marten.age.graphics.appearance.Color;
import marten.age.graphics.flat.sprite.Sprite;
import marten.age.graphics.flat.sprite.TextureSprite;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import marten.age.graphics.transform.TranslationGroup;

import org.apache.log4j.Logger;

public class Button extends Sprite implements Widget, MouseListener {
    @SuppressWarnings("unused")
    private static org.apache.log4j.Logger log = Logger.getLogger(Button.class);

    private boolean pressed = false;
    private boolean mouseOver = false;
    private Action action;
    private TextureSprite face;
    private BitmapString label;
    private BitmapFont font;
    private TranslationGroup tg = new TranslationGroup();

    public Button(ImageData data) {
        this.face = new TextureSprite(data);
        tg.addChild(face);
        this.addChild(tg);
    }

    public void setFont(Font font) {
        this.font = FontCache.getFont(font);
    }

    public void setLabel(String label) {
        this.label = new BitmapString(this.font);
        this.label.setContent(label);
        this.label.centerIn(this);
        this.tg.removeChild(this.label);
        this.tg.addChild(this.label);
    }

    @Override
    public void setPosition(Point point) {
        tg.setCoordinates(point);
    }

    @Override
    public void mouseDown(Point coords) {
        if (testHit(coords)) {
            this.pressed = true;
            this.setPosition(new Point(this.getPosition().x + 3, this
                    .getPosition().y - 3));
        }
    }

    @Override
    public void mouseMove(Point coords) {
        if (testHit(coords) && !this.mouseOver) {
            this.mouseOver = true;
            if (this.label != null) {
                this.label.setColor(new Color(0, 1.0, 0));
            }
        } else if (!testHit(coords) && this.mouseOver) {
            if (this.label != null) {
                this.label.setColor(new Color(1, 1, 1));
            }
            this.mouseOver = false;
        }
    }

    @Override
    public void mouseUp(Point coords) {
        if (this.pressed) {
            if (testHit(coords) && this.action != null) {
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

    private boolean testHit(Point coords) {
        double dx = coords.x - this.getPosition().x;
        double dy = coords.y - this.getPosition().y;
        Dimension d = this.getDimension();
        if ((dx >= 0 && dx <= d.width)
                && (dy >= 0 && dy <= d.height)) {
            return true;
        }
        return false;
    }

    @Override
    public Dimension getDimension() {
        return this.face.getDimension();
    }

    @Override
    public Point getPosition() {
        return tg.getCoordinates();
    }

}
