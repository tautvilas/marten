package marten.testing.tests.networktest;

import marten.age.control.KeyboardController;
import marten.age.core.AgeScene;
import marten.age.graphics.flat.Flatland;
import marten.age.widget.Console;
import marten.age.widget.ConsoleListener;
import marten.age.widget.Widget;

public class NetworkScene extends AgeScene {

    private Flatland flatland = null;

    public NetworkScene() {
        this.flatland = new Flatland();
        this.addController(new KeyboardController());
        Widget console = new Console(new ConsoleListener() {

            @Override
            public String handleCommand(String command) {
                System.out.println("I should handle command '" + command + "'");
                return "failed";
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
