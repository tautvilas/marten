package marten.age.graphics.model;

import java.util.ArrayList;

import marten.age.graphics.BasicSceneGraphChild;
import marten.age.graphics.appearance.Appearance;
import marten.age.graphics.geometry.Geometry;

public class SimpleModel extends BasicSceneGraphChild {
    private Appearance appearance = new Appearance();
    private ArrayList<Geometry> geometries = new ArrayList<Geometry>();

    public SimpleModel(Geometry geometry) {
        this.geometries.add(geometry);
    }

    public SimpleModel(Appearance appearance) {
        this.appearance = appearance;
    }

    public void setAppearance(Appearance appearance) {
        this.appearance = appearance;
    }

    public void addGeometry(Geometry newGeometry) {
        this.geometries.add(newGeometry);
    }

    public boolean removeGeometry(Geometry geometry) {
        if (this.geometries.contains(geometry)) {
            this.geometries.remove(geometry);
            return true;
        } else {
            return false;
        }
    }

    public Appearance getAppearance() {
        return appearance;
    }

    public ArrayList<Geometry> getGeometries() {
        return this.geometries;
    }

    public void setGeometries(ArrayList<Geometry> geometries) {
        this.geometries = geometries;
    }
 
    public Geometry getFirstGeometry() {
        if (this.geometries.size() == 0) {
            return null;
        } else {
            return this.geometries.get(0);
        }
    }

    public void render() {
//        System.out.println("simple model "  + this.getId() + " " + this.geometries.size());
        appearance.set();
        for (Geometry geometry : this.geometries) {
            geometry.render();
        }
    }
}
