package marten.age.widget;

import marten.age.control.MouseListener;
import marten.age.graphics.flat.sprite.PixelSprite;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.primitives.Point;

import org.apache.log4j.Logger;

public class Button extends PixelSprite implements Widget, MouseListener {
    @SuppressWarnings("unused")
    private static org.apache.log4j.Logger log = Logger.getLogger(Button.class);

    private boolean pressed = false;
    private Action action;

    public Button(ImageData data) {
        super(data);
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
    }

    @Override
    public void mouseUp(Point coords) {
        if (testHit(coords) && this.pressed) {
            this.action.perform();
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
        if ((dx >= 0 && dx <= this.getWidth())
                && (dy >= 0 && dy <= this.getHeight())) {
            return true;
        }
        return false;
    }
}
