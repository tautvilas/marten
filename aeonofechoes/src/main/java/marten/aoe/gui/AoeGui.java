package marten.aoe.gui;

import marten.age.core.AgeApp;
import marten.aoe.gui.scene.MainMenu;

public class AoeGui extends AgeApp {

    @Override
    public void configure() {
        this.setActiveScene(new MainMenu());
    }

}
