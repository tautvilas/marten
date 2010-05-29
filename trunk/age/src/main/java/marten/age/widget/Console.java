package marten.age.widget;

import marten.age.control.KeyboardListener;
import marten.age.graphics.BasicSceneGraphChild;

public class Console extends BasicSceneGraphChild implements Widget,
        KeyboardListener {

    @Override
    public void render() {
    }

    @Override
    public void keyDown(int key) {
        System.out.println(key);
    }

    @Override
    public void keyUp(int key) {
        System.out.println(key);
    }

}
