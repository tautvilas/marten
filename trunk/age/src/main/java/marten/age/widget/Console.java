package marten.age.widget;

import java.awt.Font;
import java.util.ArrayList;

import marten.age.control.KeyboardListener;
import marten.age.graphics.BasicSceneGraphChild;
import marten.age.graphics.appearance.Color;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class Console extends BasicSceneGraphChild implements Widget,
        KeyboardListener {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(Console.class);

    private ArrayList<LogEntry> consoleLog = new ArrayList<LogEntry>();

    private String command = "";
    private String prompt = "> ";

    private BitmapFont font = FontCache.getFont(new Font("Courier New",
            Font.BOLD, 14));

    private ConsoleListener listener = null;

    public Console(ConsoleListener listener) {
        this.listener = listener;
    }

    @Override
    public void render() {
        BitmapString text = new BitmapString(font, "");
        for (int i = 0; i < consoleLog.size(); i++) {
            text.addColor(new Color(1.0, 1.0, 1.0));
            text.addContent(consoleLog.get(i).command + "\n");
            text.addColor(new Color(0, 1.0, 0));
            text.addContent(consoleLog.get(i).response + "\n");
        }
        text.addColor(new Color(1.0, 1.0, 1.0));
        text.addContent(prompt + command);
        text.render();
    }

    @Override
    public void keyDown(int key, char character) {
        if (character >= 32 && character <= 126) {
            command += character;
        } else if (key == Keyboard.KEY_BACK) {
            if (command.length() != 0) {
                command = command.substring(0, command.length() - 1);
            }
        } else if (key == Keyboard.KEY_RETURN) {
            String response = listener.handleCommand(command);
            consoleLog.add(new LogEntry(command, response));
            command = "";
        } else {
            log.warn("Unrecognized key pressed: " + key + " ("
                    + Keyboard.getKeyName(key) + ")");
        }
    }

    @Override
    public void keyUp(int key, char character) {
    }

    private class LogEntry {
        public String command;
        public String response;
        public LogEntry(String command, String response) {
            this.command = command;
            this.response = response;
        }
    }

}
