package marten.age.graphics.model;

import java.util.ArrayList;

import marten.age.graphics.BasicSceneGraphChild;
import marten.age.graphics.appearance.Appearance;

@Deprecated
public class ComplexModel extends BasicSceneGraphChild {
    private ArrayList<SimpleModel> parts = new ArrayList<SimpleModel>();
    private Appearance appearance = new Appearance();

    public void addPart(SimpleModel newPart) {
        parts.add(newPart);
    }

    public void removePart(SimpleModel oldPart) {
        parts.remove(oldPart);
    }

    public SimpleModel getFirstPart() {
        return parts.get(0);
    }

    public void setAppearance(Appearance appearance) {
        this.appearance = appearance;
    }

    public ArrayList<SimpleModel> getParts() {
        return parts;
    }

    public void render() {
        appearance.set();
        for (SimpleModel part : parts)
            part.render();
    }
}
