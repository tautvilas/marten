package marten.age;

public class SimpleModel implements SceneGraphChild {
	private Appearance appearance = new Appearance();
	private Geometry geometry = null;
	public SimpleModel(Geometry geometry) {
		this.geometry = geometry;
	}
	public void setAppearance (Appearance appearance) {
		this.appearance = appearance;
	}
	
	public void setGeometry (Geometry newGeometry) {
		geometry = newGeometry;
	}
	
	public Appearance getAppearance() {
		return appearance;
	}

	public Geometry getGeometry () {
		return geometry;
	}
	
	public void activate () {
		appearance.set();
		geometry.draw();
	}
}
