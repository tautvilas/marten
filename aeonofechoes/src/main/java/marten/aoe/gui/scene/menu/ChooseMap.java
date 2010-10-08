package marten.aoe.gui.scene.menu;

import java.io.File;

import marten.age.control.MouseController;
import marten.age.core.AgeScene;
import marten.age.core.AppInfo;
import marten.age.event.AgeSceneSwitchEvent;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.flat.SimpleLayout;
import marten.age.graphics.primitives.Dimension;
import marten.age.widget.Action;
import marten.aoe.Path;
import marten.aoe.gui.widget.AoeButton;
import marten.aoe.gui.widget.OkCancelDialog;

public class ChooseMap extends AgeScene {

    private Flatland flatland = new Flatland();

    public ChooseMap() {
        this.addController(new MouseController());

        SimpleLayout layout = new SimpleLayout(AppInfo.getDisplayDimension());
        OkCancelDialog okCancel = new OkCancelDialog(new Dimension(600, 50));

        File mapFolder = new File(Path.MAP_PATH);
        String[] filenames = mapFolder.list();
        for (int i = 0; i < filenames.length; i++) {
            final String filename = filenames[i];
            if (filename.charAt(0) == '.') continue;
            AoeButton mapButton = new AoeButton(filename);
            mapButton.setAction(new Action() {
                @Override
                public void perform() {
                    fireEvent(new AgeSceneSwitchEvent(new GameGate(filename)));
                }
            });
            layout.centerHorizontally(mapButton, (int)(layout.getDimension().height
                    - 100 - i * mapButton.getDimension().height));
            this.registerControllable(mapButton);
        }

        okCancel.setCancelAction(new Action() {
            @Override
            public void perform() {
                fireEvent(new AgeSceneSwitchEvent(new MainMenu()));
            }
        });

        layout.centerHorizontally(okCancel, 100);
        this.registerControllable(okCancel);
        this.flatland.addChild(layout);
        this.flatland.compile();
    }

    @Override
    public void compute() {
    }

    @Override
    public void render() {
        this.flatland.render();
    }

}
