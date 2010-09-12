package marten.testing.tests.networktest;

import java.awt.Font;
import java.rmi.Naming;
import java.rmi.RemoteException;

import marten.age.control.KeyboardController;
import marten.age.core.AgeScene;
import marten.age.core.AppInfo;
import marten.age.graphics.appearance.Color;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import marten.age.widget.Console;
import marten.age.widget.ConsoleListener;
import marten.age.widget.Widget;
import marten.aoe.server.AoeServer;
import marten.aoe.server.ClientSession;
import marten.aoe.server.Server;
import marten.aoe.server.Session;

import org.apache.log4j.Logger;

public class NetworkScene extends AgeScene {
    @SuppressWarnings("unused")
    private static org.apache.log4j.Logger log = Logger
            .getLogger(NetworkScene.class);

    private Flatland flatland = null;
    private Session session = null;
    private Server server = null;
    private String username;
    private BitmapFont font = FontCache.getFont(new Font("Courier New",
            Font.BOLD, 14));
    private BitmapString messages = new BitmapString(font);

    public NetworkScene() {
        this.flatland = new Flatland();
        this.addController(new KeyboardController());
        Widget console = new Console(new ConsoleListener() {

            @Override
            public String handleCommand(String command) {
                String[] words = command.split(" ");
                if (command.equals("start server")) {
                    AoeServer.start();
                    return "Aoe server started!";
                } else if (words[0].equals("login")) {
                    if (words.length != 3) {
                        return "Usage: login [server] [username]";
                    }
                    try {
                        // TODO(zv): this should be run in new thread because
                        // application freezes while connecting to server
                        server = (Server) Naming.lookup("//" + words[1]
                                + "/Server");
                        session = new ClientSession(words[2]) {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void publishMessage(String from,
                                    String message) throws RemoteException {
                                messages.addColor(new Color(0.0, 0, 1.0));
                                messages.addContent(from);
                                messages.addColor(new Color(1.0, 1.0, 1.0));
                                messages.addContent(" to ");
                                messages.addColor(new Color(1.0, 0.0, 0.0));
                                messages.addContent(username);
                                messages.addColor(new Color(1.0, 1.0, 1.0));
                                messages.addContent(": " + message + "\n");
                            }
                        };
                        server.login(session);
                        username = words[2];
                        return "logged in";
                    } catch (Exception e) {
                        // return "Could not connect to AOE server";
                        throw new RuntimeException(e);
                    }
                } else if (words[0].equals("msg")) {
                    if (session == null) {
                        return "You should login first";
                    }
                    if (words.length < 3) {
                        return "Usage: msg [username] [message]";
                    }
                    String message = "";
                    for (int i = 2; i < words.length; i++) {
                        message += words[i] + " ";
                    }
                    try {
                        server.sendMessage(session, words[1], message);
                    } catch (RemoteException e) {
                        // log.error("Could not send message");
                        throw new RuntimeException(e);
                    }
                    messages.addColor(new Color(1.0, 0, 0));
                    messages.addContent(username);
                    messages.addColor(new Color(1.0, 1.0, 1.0));
                    messages.addContent(" to ");
                    messages.addColor(new Color(0.0, 0.0, 1.0));
                    messages.addContent(words[1]);
                    messages.addColor(new Color(1.0, 1.0, 1.0));
                    messages.addContent(": " + message + "\n");
                    return "message sent";
                } else {
                    return "Unrecognized command '" + command + "'";
                }
            }
        });
        messages.setPosition(new Point(0, AppInfo.getDisplayHeight() - 100));
        this.flatland.addChild(console);
        this.flatland.addChild(messages);
        // this.flatland.addChild(new FpsCounter());
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