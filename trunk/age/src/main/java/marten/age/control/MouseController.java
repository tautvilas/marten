package marten.age.control;

import marten.age.graphics.util.Point;

import org.lwjgl.input.Mouse;

public class MouseController extends Controller {
	
	private Point cursor = new Point();
	private int wheelD;
	private int mouseDX;
	private int mouseDY;
	private int mouseDW;
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
	
	@Override
	protected void harvestEvents() {
		wheelD = Mouse.getDWheel();
		mouseDX = Mouse.getDX();
		mouseDY = Mouse.getDY();
		mouseDW = Mouse.getDWheel();
		cursor.x = Mouse.getX();
		cursor.y = Mouse.getY();
		
		boolean mouseUpEvent = false;
		boolean mouseDownEvent = false;
		
		while (Mouse.next()) {
			if(Mouse.getEventButton() == 0 && Mouse.getEventButtonState() == true) {
				mouseDown = true;
				mouseDownEvent = true;
			}
			if(Mouse.getEventButton() == 0 && Mouse.getEventButtonState() == false) {
				mouseUp = true;
				mouseUpEvent = true;
			}
		}
		
		if (!mouseUpEvent) mouseUp = false;
		if (!mouseDownEvent) mouseDown = false;
	}

	@Override
	protected void publishEventsToListener(Listener l) {
		MouseListener listener = (MouseListener)l;
		if (wheelD != 0) {
			listener.mouseWheelRoll(wheelD);
		}
		if (mouseDX != 0 || mouseDY != 0 || mouseDW != 0) {
			listener.mouseMove(cursor);
		}
		if (mouseDown) {
			listener.mouseDown(cursor);
		}
		if (mouseUp) {
			listener.mouseUp(cursor);
		}
	}
}