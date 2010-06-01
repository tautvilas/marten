package marten.testing.tests.networktest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import marten.age.control.KeyboardController;
import marten.age.core.AgeScene;
import marten.age.graphics.flat.Flatland;
import marten.age.widget.Console;
import marten.age.widget.ConsoleListener;
import marten.age.widget.Widget;
import marten.aoe.AoeServer;

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
                    AoeServer server = new AoeServer();
                    server.start();
                    return "OK";
                } else if (command.equals("connect")) {
                    Socket socket = null;
                    PrintWriter out = null;
                    BufferedReader in = null;

                    try {
                        socket = new Socket("127.0.0.1", 4561);
                        out = new PrintWriter(socket.getOutputStream(), true);
                        in = new BufferedReader(new InputStreamReader(socket
                                .getInputStream()));
                    } catch (UnknownHostException e) {
                        log.error("Don't know about host");
                    } catch (IOException e) {
                        log.error("Couldn't get I/O for the connection");
                    }

                    try {
                        out.println("Hello");
                        String response = in.readLine();
                        log.info("Server response: " + response);

                        out.close();
                        in.close();
                        socket.close();
                    } catch (IOException e) {
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
