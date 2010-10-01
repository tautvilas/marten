package marten.aoe.gui.scene.menu;

import java.awt.Font;

import marten.age.control.KeyboardController;
import marten.age.control.MouseController;
import marten.age.core.AppInfo;
import marten.age.graphics.image.ImageCache;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import marten.age.widget.AgeField;
import marten.aoe.Path;

import org.apache.log4j.Logger;

public class JoinDialog extends MenuScene {

    @SuppressWarnings("unused")
    private static org.apache.log4j.Logger log = Logger
            .getLogger(JoinDialog.class);

    public JoinDialog() {
        super();
        BitmapFont dialogFont = FontCache.getFont(new Font("Arial", Font.PLAIN,
                20));
        ImageData fieldImage = ImageCache
                .getImage(Path.SKIN_PATH + "field.png");
        ImageData cursorImage = ImageCache.getImage(Path.SKIN_PATH
                + "cursor.png");

        BitmapString fieldLabel = new BitmapString(dialogFont,
                "Enter server location:");
        AgeField UrlField = new AgeField(fieldImage, cursorImage, FontCache
                .getFont(new Font("Arial", Font.PLAIN, 20)));

        Dimension dField = UrlField.getDimension();
        Dimension dScreen = AppInfo.getDisplayDimension();
        Point pField = new Point(dScreen.width / 2 - dField.width / 2,
                dScreen.height / 2 - dField.height / 2);
        UrlField.setPosition(pField);
        fieldLabel.setPosition(new Point(pField.x, pField.y + dField.height));

        this.flatland.addChild(UrlField);
        this.flatland.addChild(fieldLabel);
        this.flatland.compile();
        this.addController(new KeyboardController());
        this.addController(new MouseController());
        this.registerControllable(UrlField);
    }

    @Override
    public void compute() {
    }

    @Override
    public void render() {
        flatland.render();
    }

}
