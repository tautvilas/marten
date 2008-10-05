package marten.age.geometry;

import marten.age.Geometry;
import marten.age.GeometryCache;

public class ReusedGeometry implements Geometry {
	
	private Geometry geometry;
	
	public ReusedGeometry(Geometry geometry) {
		if (geometry instanceof ReusedGeometry) {
			throw new RuntimeException("Trying to reuse geometry which is allready reused!");
		}
		Geometry g2 = GeometryCache.get(geometry);
		if (g2 == null) {
			GeometryCache.add(geometry);
			this.geometry = geometry;
		} else {
			this.geometry = g2;
		}
	}

	@Override
	public void draw() {
		this.geometry.draw();
	}
}
