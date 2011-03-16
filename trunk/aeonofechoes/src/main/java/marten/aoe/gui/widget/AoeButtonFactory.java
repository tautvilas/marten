package marten.aoe.gui.widget;

import java.awt.Font;

import marten.age.graphics.appearance.Color;
import marten.age.graphics.image.ImageCache;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.FontCache;
import marten.age.widget.Button;
import marten.aoe.Path;

public class AoeButtonFactory {

    public static Button getEndTurnButton() {
        Button button = new Button(ImageCache.getImage(Path.SKIN_DATA_PATH
                + "end-turn.png"));
        BitmapFont buttonFont = FontCache.getFont(new Font("Arial", Font.PLAIN,
                14));
        button.setFont(buttonFont);
        button.setLabel("End turn", new Color(0, 0, 0));
        return button;
    }

    public static Button getMenuButton(String label) {
        Button button = new Button(ImageCache.getImage(Path.SKIN_DATA_PATH
                + "menu-button.png"));
        BitmapFont buttonFont = FontCache.getFont(new Font("Arial", Font.PLAIN,
                20));
        button.setFont(buttonFont);
        button.setLabel(label);
        return button;
    }

    public static Button getEditorButton(String label) {
        Button button = new Button(ImageCache.getImage(Path.SKIN_DATA_PATH
                + "editor-button.png"));
        BitmapFont buttonFont = FontCache.getFont(new Font("Arial", Font.PLAIN,
                16));
        button.setFont(buttonFont);
        button.setLabel(label);
        return button;
    }
}
