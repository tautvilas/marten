package marten.aoe.gui.widget;

import marten.age.graphics.appearance.Appearance;
import marten.age.graphics.appearance.Color;
import marten.age.graphics.geometry.primitives.Rectangle;
import marten.age.graphics.layout.BoxedObject;
import marten.age.graphics.model.SimpleModel;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.transform.TranslationGroup;

public class Dialog extends TranslationGroup implements BoxedObject {

    private Dimension dimension;

    public Dialog(Dimension dimension) {
        this.dimension = dimension;
        SimpleModel sm = new SimpleModel(new Appearance(new Color(0.5, 0.5, 0.5)));
        sm.addGeometry(new Rectangle(dimension));
        this.addChild(sm);
    }

    @Override
    public Dimension getDimension() {
        return this.dimension;
    }

}
