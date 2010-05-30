package marten.age.widget;

import java.awt.Font;

import marten.age.control.KeyboardListener;
import marten.age.graphics.BasicSceneGraphChild;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;

public class Console extends BasicSceneGraphChild implements Widget,
        KeyboardListener {

    private BitmapFont font = FontCache.getFont(new Font("Courier New", Font.BOLD, 12));
//    private ArrayList<String> log = new ArrayList<String>();
    private String command = "";
    private String prompt = "> ";
    private BitmapString text = new BitmapString(font, prompt);

    public Console() {
    }

    @Override
    public void render() {
        text.setContent(prompt + command);
        text.render();
    }

    @Override
    public void keyDown(int key) {
        System.out.println(key);
        if (key >= 32 && key <= 126) {
            char c = (char)(key + '0');
            command += c;
        }
    }

    @Override
    public void keyUp(int key) {
    }

}
