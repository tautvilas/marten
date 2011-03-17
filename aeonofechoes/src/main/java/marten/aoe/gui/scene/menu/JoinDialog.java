package marten.aoe.gui.scene.menu;

import java.net.InetAddress;
import java.net.UnknownHostException;

import marten.age.control.KeyboardController;
import marten.age.control.MouseController;
import marten.age.core.AppInfo;
import marten.age.event.AgeSceneSwitchEvent;
import marten.age.graphics.layout.SimpleLayout;
import marten.age.graphics.primitives.Dimension;
import marten.age.widget.Action;
import marten.aoe.gui.scene.GameGate;
import marten.aoe.gui.widget.AoeField;
import marten.aoe.gui.widget.AoeString;
import marten.aoe.gui.widget.OkCancelDialog;

import org.apache.log4j.Logger;

public class JoinDialog extends MenuScene {

    private static org.apache.log4j.Logger log = Logger
            .getLogger(JoinDialog.class);

    public JoinDialog() {
        super();
        // Constructing GUI elements
        SimpleLayout layout = new SimpleLayout(AppInfo.getDisplayDimension());
        AoeString fieldLabel = new AoeString("Enter server location:");
        final AoeField urlField = new AoeField();
        OkCancelDialog okCancel = new OkCancelDialog(new Dimension(600, 40));

        // Layouting GUI elements
        Dimension dField = urlField.getDimension();
        layout.center(urlField);
        layout.centerHorizontally(fieldLabel,
                (int)(urlField.getPosition().y + dField.height));
        layout.centerHorizontally(okCancel,
                (int)(urlField.getPosition().y - 100));

        // Setuping button actions
        okCancel.setCancelAction(new Action() {
            @Override
            public void perform() {
                fireEvent(new AgeSceneSwitchEvent(new MainMenu()));
            }
        });
        okCancel.setOkAction(new Action() {
            @Override
            public void perform() {
                String host = urlField.getValue();
                try {
                    fireEvent(new AgeSceneSwitchEvent(new GameGate(InetAddress
                            .getByName(host))));
                } catch (UnknownHostException e) {
                    log.error("Unknown host " + host);
                    throw new RuntimeException(e);
                }
            }
        });

        // Registering and adding GUI elements
        this.flatland.addChild(layout);
//        this.flatland.compile();
        this.addController(new KeyboardController());
        this.addController(new MouseController());
        this.registerControllable(urlField);
        this.registerControllable(okCancel);
    }

    @Override
    public void compute() {
    }

    @Override
    public void render() {
        flatland.render();
    }

}
