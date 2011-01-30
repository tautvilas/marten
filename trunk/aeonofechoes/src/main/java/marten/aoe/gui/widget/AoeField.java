package marten.aoe.gui.widget;

import java.awt.Font;

import marten.age.graphics.image.ImageCache;
import marten.age.graphics.text.FontCache;
import marten.age.widget.AgeField;
import marten.aoe.Path;

public class AoeField extends AgeField {
    public AoeField() {
        super(ImageCache.getImage(Path.SKIN_DATA_PATH + "field.png"), ImageCache
                .getImage(Path.SKIN_DATA_PATH + "cursor.png"), FontCache
                .getFont(new Font("Arial", Font.PLAIN, 20)));
    }
}
