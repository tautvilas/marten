package marten.age.graphics.model;

import marten.age.graphics.BasicSceneGraphChild;
import marten.age.graphics.appearance.Appearance;
import marten.age.graphics.geometry.Geometry;

public class SimpleModel extends BasicSceneGraphChild {
    private Appearance appearance = new Appearance();
    private Geometry geometry = null;

    public SimpleModel(Geometry geometry) {
        this.geometry = geometry;
    }

    public void setAppearance(Appearance appearance) {
        this.appearance = appearance;
    }

    public void setGeometry(Geometry newGeometry) {
        geometry = newGeometry;
    }

    public Appearance getAppearance() {
        return appearance;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void render() {
        appearance.set();
        geometry.draw();
    }
}
