package marten.aoe;

import marten.age.core.AgeApp;
import marten.aoe.gui.scene.GameGate;

public class DebugMapRun extends AgeApp {

    public DebugMapRun(String title, boolean show) {
        super(title, show);
    }

    public static void main(String[] args) {
        new DebugMapRun("2p debug map", false).execute();
    }

    @Override
    public void configure() {
        GameInfo.nickname = "zvitruolis";
        this.setActiveScene(new GameGate("2pDebug"));
        this.setCursor(Path.SKIN_DATA_PATH + "pointer.png");
//        this.setMouseGrabbed(true);
    }
}
