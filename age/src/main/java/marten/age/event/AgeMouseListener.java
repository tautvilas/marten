package marten.age.event;

import marten.util.Point;

public interface AgeMouseListener {
	public void mouseMove(Point coords);
	
	public void mouseUp(Point coords);
	
	public void mouseDown(Point coords);
}
