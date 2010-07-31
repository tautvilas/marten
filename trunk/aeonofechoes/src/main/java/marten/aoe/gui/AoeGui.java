package marten.aoe.gui;

import marten.age.core.AgeApp;
import marten.aoe.gui.scene.Game;

public class AoeGui extends AgeApp {

    @Override
    public void configure() {
//        this.setActiveScene(new MainMenu());
        this.setActiveScene(new Game("mapTest"));
    }

}
