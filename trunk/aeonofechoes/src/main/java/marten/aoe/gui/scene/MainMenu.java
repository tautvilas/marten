package marten.aoe.gui.scene;

import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import marten.age.control.KeyboardController;
import marten.age.control.MouseController;
import marten.age.core.AgeScene;
import marten.age.core.AppInfo;
import marten.age.event.AgeSceneSwitchEvent;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.image.ImageCache;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import marten.age.widget.Action;
import marten.age.widget.AgeField;
import marten.age.widget.Button;
import marten.age.widget.obsolete.FpsCounter;
import marten.aoe.Path;

import org.apache.log4j.Logger;

public class MainMenu extends AgeScene {
    @SuppressWarnings("unused")
    private static org.apache.log4j.Logger log = Logger
            .getLogger(MainMenu.class);

    private Flatland flatland = new Flatland();

    private String generateNick() {
        File file = new File("/usr/share/dict/words");
        String nick = "";
        ArrayList<String> lines = new ArrayList<String>();

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lines.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "nickname";
        }

        for (int i = 0; i < 2; i++) {
            String line = lines.get((int)(Math.random() * lines.size()));
            while (line.length() < 3) {
                line = lines.get((int)(Math.random() * lines.size()));
            }
            line.toLowerCase();
            nick += line;
            if (i == 0) {
                nick += "_";
            }
        }
        return nick;
    }

    public MainMenu() {
        // create fonts
        BitmapFont titleFont = FontCache.getFont(new Font("Arial", Font.PLAIN,
                50));
        BitmapFont dialogFont = FontCache.getFont(new Font("Arial", Font.PLAIN,
                20));

        // load graphics
        ImageData menuButtonImage = ImageCache.getImage(Path.SKIN_PATH
                + "menu-button.png");
        ImageData fieldFace = ImageCache.getImage(Path.SKIN_PATH + "field.png");
        ImageData cursor = ImageCache.getImage(Path.SKIN_PATH + "cursor.png");

        // create and configure graphic elements
        BitmapString nickString = new BitmapString(dialogFont, "Your nickname:");
        BitmapString title = new BitmapString(titleFont, "Aeon Of Echoes");
        // title.setColor(new Color(0.0, 1.0, 0.0));
        Button hostButton = new Button(menuButtonImage);
        Button joinButton = new Button(menuButtonImage);
        hostButton.setFont(dialogFont);
        hostButton.setLabel("Host Game");
        joinButton.setFont(dialogFont);
        joinButton.setLabel("Join Game");
        AgeField nickField = new AgeField(fieldFace, cursor, dialogFont);
        nickField.setValue(generateNick());

        // position graphic elements
        Dimension dButton = hostButton.getDimension();
        Dimension dApp = AppInfo.getDisplayDimension();
        Dimension dField = nickField.getDimension();
        hostButton.setPosition(new Point(dApp.width / 2 - dButton.width / 2,
                dApp.height / 2 - dButton.height / 2));
        joinButton.setPosition(new Point(dApp.width / 2 - dButton.width / 2,
                dApp.height / 2 - dButton.height * 2));
        Dimension dTitle = title.getDimension();
        title.setPosition(new Point(dApp.width / 2 - dTitle.width / 2,
                dApp.height / 2 + dButton.height * 3));
        nickField.setPosition(new Point(dApp.width / 2 - dField.width / 2,
                dApp.height / 2 + dButton.height));
        nickString.setPosition(new Point(dApp.width / 2 - dField.width / 2,
                dApp.height / 2 + dButton.height + dField.height));

        // hook up actions
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

        // register graphic elements
        this.addController(new KeyboardController());
        this.addController(new MouseController());
        this.registerControllable(hostButton);
        this.registerControllable(joinButton);
        this.registerControllable(nickField);

        // add graphic elements to flatland
        this.flatland.addChild(title);
        this.flatland.addChild(nickString);
        this.flatland.addChild(nickField);
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
