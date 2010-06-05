package marten.testing.tests.networktest;

import java.rmi.Naming;

import marten.age.control.KeyboardController;
import marten.age.core.AgeScene;
import marten.age.graphics.flat.Flatland;
import marten.age.widget.Console;
import marten.age.widget.ConsoleListener;
import marten.age.widget.Widget;
import marten.aoe.server.AoeServer;
import marten.aoe.server.Server;

import org.apache.log4j.Logger;

public class NetworkScene extends AgeScene {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(NetworkScene.class);

    private Flatland flatland = null;

    public NetworkScene() {
        this.flatland = new Flatland();
        this.addController(new KeyboardController());
        Widget console = new Console(new ConsoleListener() {

            @Override
            public String handleCommand(String command) {
                if (command.equals("start server")) {
                    AoeServer.start();
                    return "OK";
                } else if (command.equals("connect")) {
                    try {
                        Server server = (Server)Naming.lookup("//localhost/Server");
                        log.info("Server says: " + server.say());
                    } catch (Exception e) {
                        log.error("Could not connect to AOE server");
                        throw new RuntimeException(e);
                    }
                } else {
                    log.warn("Unrecognized command '" + command + "'");
                }
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
