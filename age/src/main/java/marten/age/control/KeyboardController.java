package marten.age.control;

import org.lwjgl.input.Keyboard;

public class KeyboardController extends Controller {

	public KeyboardController() {
	}
	
	public KeyboardController(KeyboardListener l) {
		super.addListener(l);
	}
	
	public void addListener(KeyboardListener l) {
		super.addListener(l);
	}

	@Override
	protected void publishEventsToListener(Listener l) {
		KeyboardListener listener = (KeyboardListener)l;
		while ( Keyboard.next() )  {
			if (Keyboard.getEventKeyState()) {
				listener.keyDown(Keyboard.getEventKey());
			}
			else {
				listener.keyUp(Keyboard.getEventKey());
			}
		}
	}

	@Override
	protected void harvestEvents() {
	}
}
