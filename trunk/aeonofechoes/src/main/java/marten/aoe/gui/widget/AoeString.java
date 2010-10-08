package marten.aoe.gui.widget;

import java.awt.Font;

import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;

public class AoeString extends BitmapString {

    public AoeString(String content) {
        super(FontCache.getFont(new Font("Arial", Font.PLAIN, 20)), content);
    }

}
