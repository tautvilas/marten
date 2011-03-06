package marten.aoe.gui.widget;

import java.awt.Font;

import marten.age.graphics.appearance.Color;
import marten.age.graphics.layout.BoxedObject;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import marten.age.graphics.transform.TranslationGroup;
import marten.age.widget.Widget;
import marten.aoe.dto.UnitDTO;

public class UnitWidget extends TranslationGroup implements Widget, BoxedObject {

    private BitmapString representation;
//    private UnitDTO unit;
    private final BitmapFont font = FontCache.getFont(new Font("Courier New",
            Font.BOLD, 20));

    public UnitWidget(UnitDTO unit) {
//        this.unit = unit;
        this.representation = new BitmapString(font, unit.getName()
                .charAt(0) + "", new Color(0.0, 1.0, 0.0));
        this.addChild(representation);
    }

    @Override
    public Dimension getDimension() {
        return this.representation.getDimension();
    }
}
