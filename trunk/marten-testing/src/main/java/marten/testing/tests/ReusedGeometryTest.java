package marten.testing.tests;

import marten.age.Geometry;
import marten.age.geometry.OptimizedGeometry;
import marten.age.geometry.ReusedGeometry;
import marten.age.geometry.SphereGeometry;
import marten.testing.ConsoleTest;

public class ReusedGeometryTest implements ConsoleTest {
	
	Geometry g1;
	Geometry g2;
	Geometry g3;

	public void run() {
		g1 = new SphereGeometry(1.0);
		g1 = new ReusedGeometry(g1);
		g2 = new SphereGeometry(2.0);
		g2 = new ReusedGeometry(g2);
		
		g3 = new ReusedGeometry(new SphereGeometry(3.0));
		g2 = new ReusedGeometry(new SphereGeometry(2.0));
		g1 = new ReusedGeometry(new OptimizedGeometry(new SphereGeometry(1.0)));
		g1 = new ReusedGeometry(new OptimizedGeometry(new SphereGeometry(1.0)));
	}

}
