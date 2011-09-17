package marten.aoe;

import marten.age.core.AgeApp;
import marten.aoe.gui.scene.editor.MapEditorLoader;

public class AoeMapEditor extends AgeApp {

    public static void main(String[] args) {
        new AoeMapEditor().execute();
    }

    @Override
    public void configure() {
        this.setActiveScene(new MapEditorLoader());
        this.setCursor(Path.SKIN_DATA_PATH + "pointer.png");
    }

}
