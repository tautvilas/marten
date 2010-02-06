package marten.age.graphics.util;

import marten.util.Point;
import marten.util.Vector;

public final class Vertex {
	public Point coordinates = new Point();
	public Vector normal = new Vector();
	public TextureCoordinate textureCoordinates = new TextureCoordinate(0.0, 0.0, 0.0);
}
