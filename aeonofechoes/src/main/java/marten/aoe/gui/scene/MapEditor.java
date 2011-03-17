package marten.aoe.gui.scene;

import java.awt.Font;

import marten.age.control.MouseController;
import marten.age.core.AgeScene;
import marten.age.core.AppInfo;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.layout.BoxedObject;
import marten.age.graphics.layout.SimpleLayout;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import marten.age.widget.Action;
import marten.age.widget.Button;
import marten.age.widget.obsolete.FpsCounter;
import marten.aoe.gui.widget.AoeButtonFactory;
import marten.aoe.gui.widget.AoeField;
import marten.aoe.gui.widget.Dialog;
import marten.aoe.gui.widget.OkCancelDialog;
import marten.aoe.gui.widget.Sidebar;

public class MapEditor extends AgeScene {

    BitmapFont font = FontCache.getFont(new Font("Arial", Font.PLAIN, 16));

    private Flatland flatland = new Flatland();
    private NewMapDialog newMapDialog;
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
        // new button
        newButton.setPosition(new Point(0, windowHeight - buttonHeight));
        this.flatland.addChild(newButton);
        newButton.setAction(new Action() {
            @Override
            public void perform() {
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
        newMapDialog = new NewMapDialog();
    }

    private class NewMapDialog extends Dialog implements BoxedObject {

        public NewMapDialog() {
            super(new Dimension(500, 300));
            SimpleLayout container = new SimpleLayout();
            // ok cancel
            OkCancelDialog okCancel = new OkCancelDialog();
            container.addChild(okCancel);
            okCancel.setCancelAction(new Action() {
                @Override
                public void perform() {
                    MapEditor.this.layout.removeChild(NewMapDialog.this);
                }
            });
            MapEditor.this.registerControllable(okCancel);
            // field
            AoeField field = new AoeField();
            field.setPosition(new Point(0, +okCancel.getDimension().height));
            container.addChild(field);
            // text
            BitmapString text = new BitmapString(font, "Please enter map size");
            text.setPosition(new Point(0, okCancel.getDimension().height
                    + field.getDimension().height));
            container.addChild(text);
            this.center(container);
        }

    }

    @Override
    public void compute() {
    }

    @Override
    public void render() {
        this.flatland.render();
    }

}
