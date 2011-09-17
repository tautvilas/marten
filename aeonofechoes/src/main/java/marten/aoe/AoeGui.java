package marten.aoe;

import marten.age.core.AgeApp;
import marten.aoe.gui.scene.menu.MainMenu;

public class AoeGui extends AgeApp {
    public static void main(String[] args) {
        new AoeGui().execute();
    }

    @Override
    public void configure() {
        this.setActiveScene(new MainMenu());
        this.setCursor(Path.SKIN_DATA_PATH + "pointer.png");
    }

}
