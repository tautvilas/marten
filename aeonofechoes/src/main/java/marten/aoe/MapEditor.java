package marten.aoe;

import marten.age.core.AgeApp;
import marten.aoe.gui.scene.editor.MapEditorLoader;

public class MapEditor extends AgeApp {

    public static void main(String[] args) {
        new MapEditor().execute();
    }

    @Override
    public void configure() {
        this.setActiveScene(new MapEditorLoader());
    }

}
