package marten.aoe;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;

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
        boolean server = true;
        try {
            Naming.lookup("rmi://127.0.0.1/Server");
            server = false;
        } catch (Exception e) {
        }
        if (server) {
            GameInfo.nickname = "zvitruolis";
            this.setActiveScene(new GameGate("2pDebug"));
        } else {
            InetAddress addr;
            try {
                addr = InetAddress.getByName("localhost");
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
            GameInfo.nickname = "svecias";
            this.setActiveScene(new GameGate(addr));
        }
        this.setCursor(Path.SKIN_DATA_PATH + "pointer.png");
//        this.setMouseGrabbed(true);
    }
}
