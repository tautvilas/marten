package marten.aoe.gui.widget;

import marten.age.graphics.appearance.Appearance;
import marten.age.graphics.appearance.Color;
import marten.age.graphics.geometry.primitives.Rectangle;
import marten.age.graphics.layout.SimpleLayout;
import marten.age.graphics.model.SimpleModel;
import marten.age.graphics.primitives.Dimension;

public class Dialog extends SimpleLayout {

    public Dialog(Dimension dimension) {
        super(dimension);
        SimpleModel sm = new SimpleModel(new Appearance(new Color(0.5, 0.5, 0.5)));
        sm.addGeometry(new Rectangle(dimension));
        this.addChild(sm);
    }

}
