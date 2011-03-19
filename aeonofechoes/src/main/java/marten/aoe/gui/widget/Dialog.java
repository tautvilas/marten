package marten.aoe.gui.widget;

import marten.age.graphics.appearance.Appearance;
import marten.age.graphics.appearance.Color;
import marten.age.graphics.geometry.primitives.Rectangle;
import marten.age.graphics.layout.BoxedObject;
import marten.age.graphics.layout.SimpleLayout;
import marten.age.graphics.primitives.Dimension;

public class Dialog extends SimpleLayout implements BoxedObject {

    private Appearance appearance = new Appearance();

    public Dialog(Dimension dimension, Color color) {
        super(dimension);
        appearance.setColor(color);
        this.addChild(new Rectangle(dimension));
    }

    public Dialog(Dimension dimension) {
        this(dimension, new Color(0.5, 0.5, 0.5));
    }

    @Override
    public void render() {
        this.appearance.set();
        super.render();
    }
}
