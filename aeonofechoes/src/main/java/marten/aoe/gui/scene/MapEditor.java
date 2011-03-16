package marten.aoe.gui.scene;

import marten.age.core.AgeScene;
import marten.age.core.AppInfo;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.widget.Button;
import marten.age.widget.obsolete.FpsCounter;
import marten.aoe.gui.widget.AoeButtonFactory;
import marten.aoe.gui.widget.Sidebar;

public class MapEditor extends AgeScene {

    private Flatland flatland = new Flatland();

    public MapEditor() {
        Button newButton = AoeButtonFactory.getEditorButton("New");
        int buttonWidth = (int)newButton.getDimension().width;
        int buttonHeight = (int)newButton.getDimension().height;
        int windowHeight = AppInfo.getDisplayHeight();
        int windowWidth = AppInfo.getDisplayWidth();
        int padding = 5;
        // new button
        newButton.setPosition(new Point(0, windowHeight - buttonHeight));
        this.flatland.addChild(newButton);
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
        // fps counter
        this.flatland.addChild(new FpsCounter());
    }

    @Override
    public void compute() {
    }

    @Override
    public void render() {
        this.flatland.render();
    }

}
