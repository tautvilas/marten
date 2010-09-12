package marten.aoe.gui.scene;

import java.awt.Font;
import java.io.IOException;

import marten.age.control.MouseController;
import marten.age.core.AgeScene;
import marten.age.core.AppInfo;
import marten.age.event.AgeSceneSwitchEvent;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import marten.age.widget.Action;
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
        // load graphics
        ImageData menuButtonImage;
        try {
            menuButtonImage = new ImageData(Path.SKIN_PATH + "menu-button.png");
        } catch (IOException e) {
            log.error("Could not load menu button image.");
            throw new RuntimeException(e);
        }

        // create and configure title
        BitmapString title = new BitmapString(titleFont, "Aeon Of Echoes");
        // title.setColor(new Color(0.0, 1.0, 0.0));

        // create and configure buttons
        Button hostButton = new Button(menuButtonImage);
        Button joinButton = new Button(menuButtonImage);
        Font font = new Font("Arial", Font.PLAIN, 20);
        hostButton.setFont(font);
        hostButton.setLabel("Host Game");
        joinButton.setFont(font);
        joinButton.setLabel("Join Game");

        // center the buttons and the title
        Dimension dButton = hostButton.getDimension();
        hostButton.setPosition(new Point(AppInfo.getDisplayWidth() / 2
                - dButton.width / 2, AppInfo.getDisplayHeight() / 2
                + dButton.height));
        joinButton.setPosition(new Point(AppInfo.getDisplayWidth() / 2
                - dButton.width / 2, AppInfo.getDisplayHeight() / 2
                - dButton.height));
        Dimension dTitle = title.getDimension();
        title.setPosition(new Point(AppInfo.getDisplayWidth() / 2
                - dTitle.width / 2, AppInfo.getDisplayHeight() / 2
                + dButton.height * 3));

        // hook up button actions
        hostButton.setAction(new Action() {
            @Override
            public void perform() {
                fireEvent(new AgeSceneSwitchEvent(new HostDialog()));
            }
        });
        joinButton.setAction(new Action() {
            @Override
            public void perform() {
                fireEvent(new AgeSceneSwitchEvent(new JoinDialog()));
            }
        });

        // register buttons
        // this.addController(new KeyboardController());
        this.addController(new MouseController());
        this.registerControllable(hostButton);
        this.registerControllable(joinButton);

        // add graphic elements to flatland
        this.flatland.addChild(title);
        this.flatland.addChild(hostButton);
        this.flatland.addChild(joinButton);
        this.flatland.addChild(new FpsCounter());
        this.flatland.compile();
    }

    @Override
    public void compute() {
    }

    @Override
    public void render() {
        flatland.render();
    }
}
