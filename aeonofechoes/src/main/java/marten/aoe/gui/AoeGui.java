package marten.aoe.gui;

import marten.age.core.AgeApp;
import marten.aoe.Path;
import marten.aoe.gui.scene.menu.MainMenu;

public class AoeGui extends AgeApp {

    @Override
    public void configure() {
        this.setActiveScene(new MainMenu());
        this.setCursor(Path.SKIN_DATA_PATH + "pointer.png");
    }

}
