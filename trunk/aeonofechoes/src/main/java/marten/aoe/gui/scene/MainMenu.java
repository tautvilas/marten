package marten.aoe.gui.scene;

import marten.age.control.KeyboardController;
import marten.age.control.MouseController;
import marten.age.core.AgeScene;
import marten.age.event.AgeSceneSwitchEvent;
import marten.age.graphics.flat.Flatland;
import marten.age.widget.Console;
import marten.age.widget.ConsoleListener;
import marten.age.widget.Widget;

import org.apache.log4j.Logger;

public class MainMenu extends AgeScene {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(MainMenu.class);

    private Flatland flatland = new Flatland();

    public MainMenu() {
        this.addController(new KeyboardController());
        this.addController(new MouseController());
        Widget console = new Console(new ConsoleListener() {
            @Override
            public String handleCommand(String command) {
                if (command.equals("start")) {
                    fireEvent(new AgeSceneSwitchEvent(new Game("Village")));
                    return "WIN";
                }
                log.info("No such command!");
                return "FAIL";
            }
        });
        this.flatland.addChild(console);
        this.registerControllable(console);
    }

    @Override
    public void compute() {
    }

    @Override
    public void render() {
        flatland.render();
    }
}
