package marten.age.control;

import marten.age.graphics.primitives.Point;

import org.lwjgl.input.Mouse;

public class MouseController extends Controller {

    private Point cursor = new Point();
    private int wheelD;
    private int mouseDX;
    private int mouseDY;
    private boolean mouseDown = false;
    private boolean mouseUp = false;

    public MouseController() {
    }

    public MouseController(MouseListener l) {
        super.addListener(l);
    }

    public void addListener(MouseListener l) {
        super.addListener(l);
    }

    public Point getMouseCoordinates() {
        return new Point(Mouse.getX(), Mouse.getY());
    }

    @Override
    protected void harvestEvents() {
        wheelD = Mouse.getDWheel();
        mouseDX = Mouse.getDX();
        mouseDY = Mouse.getDY();
        cursor.x = Mouse.getX();
        cursor.y = Mouse.getY();

        boolean mouseUpEvent = false;
        boolean mouseDownEvent = false;

        while (Mouse.next()) {
            if (Mouse.getEventButton() == 0
                    && Mouse.getEventButtonState() == true) {
                mouseDown = true;
                mouseDownEvent = true;
            }
            if (Mouse.getEventButton() == 0
                    && Mouse.getEventButtonState() == false) {
                mouseUp = true;
                mouseUpEvent = true;
            }
        }

        if (!mouseUpEvent)
            mouseUp = false;
        if (!mouseDownEvent)
            mouseDown = false;
    }

    @Override
    protected void publishEventsToListener(Listener l) {
        Point coordinates = new Point(this.cursor);
        MouseListener listener = (MouseListener) l;
        if (wheelD != 0) {
            listener.mouseWheelRoll(wheelD, coordinates);
        }
        if (mouseDX != 0 || mouseDY != 0) {
            listener.mouseMove(coordinates);
        }
        if (mouseDown) {
            listener.mouseDown(coordinates);
        }
        if (mouseUp) {
            listener.mouseUp(coordinates);
        }
    }

    @Override
    protected void listenerTypeCheck(Listener l) {
        if (!(l instanceof MouseListener)) {
            throw new ClassCastException("Incorrect listener type");
        }
    }
}
