package marten.age.widget;

import java.awt.Font;
import java.util.ArrayList;

import marten.age.control.KeyboardListener;
import marten.age.graphics.BasicSceneGraphChild;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class Console extends BasicSceneGraphChild implements Widget,
        KeyboardListener {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(Console.class);

    private ArrayList<String> consoleLog = new ArrayList<String>();

    private String command = "";
    private String prompt = "> ";

    private BitmapFont font = FontCache.getFont(new Font("Courier New",
            Font.BOLD, 12));
    private BitmapString text = new BitmapString(font, prompt);

    private ConsoleListener listener = null;

    public Console(ConsoleListener listener) {
        this.listener = listener;
    }

    @Override
    public void render() {
        text.setContent(prompt + command);
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
            consoleLog.add(command);
            listener.handleCommand(command);
            command = "";
        } else {
            log.warn("Unrecognized key pressed: " + key + " ("
                    + Keyboard.getKeyName(key) + ")");
        }
    }

    @Override
    public void keyUp(int key, char character) {
    }

}
