package marten.age.widget;

import marten.age.control.MouseListener;
import marten.age.graphics.flat.Sprite;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.primitives.Point;

import org.apache.log4j.Logger;

public class Button extends Sprite implements Widget, MouseListener {
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
            this.setX(this.getX() + 3);
            this.setY(this.getY() - 3);
        }
    }

    @Override
    public void mouseMove(Point coords) {
    }

    @Override
    public void mouseUp(Point coords) {
        if (testHit(coords) && this.pressed) {
            this.action.perform();
            this.setX(this.getX() - 3);
            this.setY(this.getY() + 3);
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
        double dx = coords.x - this.getX();
        double dy = coords.y - this.getY();
        if ((dx >= 0 && dx <= this.getWidth()) &&
                (dy >= 0 && dy <= this.getHeight())) {
            return true;
        }
        return false;
    }
}
