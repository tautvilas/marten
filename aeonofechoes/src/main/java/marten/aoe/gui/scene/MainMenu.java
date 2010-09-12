package marten.aoe.gui.scene;

import java.awt.Font;
import java.io.IOException;

import marten.age.control.KeyboardController;
import marten.age.control.MouseController;
import marten.age.core.AgeScene;
import marten.age.core.AppInfo;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import marten.age.widget.Button;
import marten.age.widget.obsolete.FpsCounter;
import marten.aoe.Path;

import org.apache.log4j.Logger;

public class MainMenu extends AgeScene {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(MainMenu.class);

    private Flatland flatland = new Flatland();
    private BitmapFont titleFont = FontCache.getFont(new Font("Arial",
            Font.PLAIN, 50));

    public MainMenu() {
        this.addController(new KeyboardController());
        this.addController(new MouseController());
        ImageData menuButtonImage;
        try {
            menuButtonImage = new ImageData(Path.SKIN_PATH + "menu-button.png");
        } catch (IOException e) {
            log.error("Could not load menu button image.");
            throw new RuntimeException(e);
        }
        BitmapString title = new BitmapString(titleFont, "Aeon Of Echoes");
//        title.setColor(new Color(0.0, 1.0, 0.0));
        Button hostButton = new Button(menuButtonImage);
        Button joinButton = new Button(menuButtonImage);
        hostButton.setLabel("Host Game");
        joinButton.setLabel("Join Game");
        // center the buttons and the title
        hostButton.setPosition(new Point(AppInfo.getDisplayWidth() / 2
                - hostButton.getWidth() / 2, AppInfo.getDisplayHeight() / 2
                + hostButton.getHeight()));
        joinButton.setPosition(new Point(AppInfo.getDisplayWidth() / 2
                - hostButton.getWidth() / 2, AppInfo.getDisplayHeight() / 2
                - joinButton.getHeight()));
        title.setPosition(new Point(AppInfo.getDisplayWidth() / 2
                - title.getWidth() / 2, AppInfo.getDisplayHeight() / 2
                + hostButton.getHeight() * 3));

        this.flatland.addChild(title);
        this.flatland.addChild(hostButton);
        this.flatland.addChild(joinButton);
        this.flatland.addChild(new FpsCounter());
        this.flatland.compile();
        this.registerControllable(hostButton);
        this.registerControllable(joinButton);
    }

    @Override
    public void compute() {
    }

    @Override
    public void render() {
        flatland.render();
    }
}
