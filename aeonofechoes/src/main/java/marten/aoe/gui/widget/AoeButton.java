package marten.aoe.gui.widget;

import java.awt.Font;

import marten.age.graphics.image.ImageCache;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.FontCache;
import marten.age.widget.Button;
import marten.aoe.Path;

public class AoeButton extends Button {

    public AoeButton(String label) {
        super(ImageCache.getImage(Path.SKIN_PATH + "menu-button.png"));
        BitmapFont buttonFont = FontCache.getFont(new Font("Arial", Font.PLAIN,
                20));
        this.setFont(buttonFont);
        this.setLabel(label);
    }
}
