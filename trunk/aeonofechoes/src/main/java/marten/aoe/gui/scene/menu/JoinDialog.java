package marten.aoe.gui.scene.menu;

import java.awt.Font;

import marten.age.control.KeyboardController;
import marten.age.control.MouseController;
import marten.age.core.AppInfo;
import marten.age.event.AgeSceneSwitchEvent;
import marten.age.graphics.flat.SimpleLayout;
import marten.age.graphics.image.ImageCache;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import marten.age.widget.Action;
import marten.age.widget.AgeField;
import marten.aoe.Path;
import marten.aoe.gui.widget.OkCancelDialog;

import org.apache.log4j.Logger;

public class JoinDialog extends MenuScene {

    @SuppressWarnings("unused")
    private static org.apache.log4j.Logger log = Logger
            .getLogger(JoinDialog.class);

    private AgeField urlField;

    public JoinDialog() {
        super();
        // Loading graphics
        BitmapFont dialogFont = FontCache.getFont(new Font("Arial", Font.PLAIN,
                20));
        ImageData fieldImage = ImageCache
                .getImage(Path.SKIN_PATH + "field.png");
        ImageData cursorImage = ImageCache.getImage(Path.SKIN_PATH
                + "cursor.png");

        // Constructing GUI elements
        SimpleLayout layout = new SimpleLayout(AppInfo.getDisplayDimension());
        BitmapString fieldLabel = new BitmapString(dialogFont,
                "Enter server location:");
        urlField = new AgeField(fieldImage, cursorImage, dialogFont);
        OkCancelDialog okCancel = new OkCancelDialog(new Dimension(600, 40));

        // Layouting GUI elements
        Dimension dField = urlField.getDimension();
        layout.center(urlField);
        layout.centerHorizontally(fieldLabel,
                (int) (urlField.getPosition().y + dField.height));
        layout.centerHorizontally(okCancel,
                (int) (urlField.getPosition().y - 100));

        // Setuping button actions
        okCancel.setCancelAction(new Action() {
            @Override
            public void perform() {
                fireEvent(new AgeSceneSwitchEvent(new MainMenu()));
            }
        });
        okCancel.setOkAction(new Action() {
            @Override
            public void perform() {
                fireEvent(new AgeSceneSwitchEvent(new GameGate(urlField
                        .getValue())));
            }
        });

        // Registering and adding GUI elements
        this.flatland.addChild(layout);
        this.flatland.compile();
        this.addController(new KeyboardController());
        this.addController(new MouseController());
        this.registerControllable(urlField);
        this.registerControllable(okCancel);
    }

    @Override
    public void compute() {
    }

    @Override
    public void render() {
        flatland.render();
    }

}
