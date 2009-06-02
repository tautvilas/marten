package marten.testing.tests;

import marten.age.geometry.Geometry;
import marten.age.geometry.OptimizedGeometry;
import marten.age.geometry.ReusedGeometry;
import marten.age.geometry.primitives.Sphere;
import marten.testing.ConsoleTest;

public class ReusedGeometryTest implements ConsoleTest {
	
	Geometry g1;
	Geometry g2;
	Geometry g3;

	public void run() {
		g1 = new Sphere(1.0);
		g1 = new ReusedGeometry(g1);
		g2 = new Sphere(2.0);
		g2 = new ReusedGeometry(g2);
		
		g3 = new ReusedGeometry(new Sphere(3.0));
		g2 = new ReusedGeometry(new Sphere(2.0));
		g1 = new ReusedGeometry(new OptimizedGeometry(new Sphere(1.0)));
		g1 = new ReusedGeometry(new OptimizedGeometry(new Sphere(1.0)));
	}

}
