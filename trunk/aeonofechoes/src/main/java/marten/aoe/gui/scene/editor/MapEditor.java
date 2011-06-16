package marten.aoe.gui.scene.editor;

import java.awt.Font;

import marten.age.control.KeyboardController;
import marten.age.control.MouseController;
import marten.age.core.AgeScene;
import marten.age.core.AppInfo;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.layout.SimpleLayout;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.FontCache;
import marten.age.widget.Action;
import marten.age.widget.Button;
import marten.age.widget.obsolete.FpsCounter;
import marten.aoe.gui.widget.AoeButtonFactory;
import marten.aoe.gui.widget.MapWidget;
import marten.aoe.gui.widget.Sidebar;

public class MapEditor extends AgeScene {

    BitmapFont font = FontCache.getFont(new Font("Arial", Font.PLAIN, 16));

    private Flatland flatland = new Flatland();
    private NewMapDialog newMapDialog;
    @SuppressWarnings("unused")
    private int mapSize = 0;
    private SimpleLayout layout = new SimpleLayout(AppInfo
            .getDisplayDimension());

    public MapEditor() {
        Button newButton = AoeButtonFactory.getEditorButton("New");
        int buttonWidth = (int)newButton.getDimension().width;
        int buttonHeight = (int)newButton.getDimension().height;
        int windowHeight = AppInfo.getDisplayHeight();
        int windowWidth = AppInfo.getDisplayWidth();
        int padding = 5;
        this.addController(new MouseController());
        this.addController(new KeyboardController());
        // new button
        newButton.setPosition(new Point(0, windowHeight - buttonHeight));
        this.flatland.addChild(newButton);
        newButton.setAction(new Action() {
            @Override
            public void perform() {
                newMapDialog.setup();
                MapEditor.this.layout.center(newMapDialog);
            }
        });
        this.registerControllable(newButton);
        // load button
        Button loadButton = AoeButtonFactory.getEditorButton("Load");
        loadButton.setPosition(new Point(buttonWidth + padding, windowHeight
                - buttonHeight));
        this.flatland.addChild(loadButton);
        // save button
        Button saveButton = AoeButtonFactory.getEditorButton("Save");
        saveButton.setPosition(new Point((buttonWidth + padding) * 2,
                windowHeight - buttonHeight));
        this.flatland.addChild(saveButton);
        // sidebar
        Sidebar sidebar = new Sidebar(new Dimension(256, windowHeight));
        sidebar.setPosition(new Point(windowWidth - 256, 0));
        flatland.addChild(sidebar);
        // other stuff
        this.flatland.addChild(new FpsCounter());
        this.flatland.addChild(layout);
        newMapDialog = new NewMapDialog(this, new Action() {
            @Override
            public void perform() {
                MapWidget map = new MapWidget(
                        newMapDialog.getMapSize(), AppInfo
                                .getDisplayDimension());
                map.setId("map");
                MapEditor.this.flatland.updateChild(map, 0);
                MapEditor.this.updateControllable(map.getId(), map);
            }
        });
    }

    @Override
    public void compute() {
    }

    @Override
    public void render() {
        this.flatland.render();
    }

}
