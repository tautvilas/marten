package marten.aoe.gui.scene.editor;

import java.awt.Font;

import marten.age.core.AgeScene;
import marten.age.graphics.appearance.Color;
import marten.age.graphics.layout.BoxedObject;
import marten.age.graphics.layout.SimpleLayout;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import marten.age.widget.Action;
import marten.age.widget.AgeField;
import marten.aoe.gui.widget.AoeField;
import marten.aoe.gui.widget.Dialog;
import marten.aoe.gui.widget.OkCancelDialog;

public class NewMapDialog extends Dialog implements BoxedObject {

    BitmapFont font = FontCache.getFont(new Font("Arial", Font.PLAIN, 16));

    private SimpleLayout container;
    private final AgeField field;
    private OkCancelDialog okCancel;
    private AgeScene parent;
    private BitmapString error;

    public NewMapDialog(AgeScene parent) {
        super(new Dimension(500, 300));
        this.error = new BitmapString(font);
        this.error.setColor(new Color(1.0, 0.0, 0.0));
        this.error.setPosition(new Point(100, 0));
        this.parent = parent;
        container = new SimpleLayout();
        // ok cancel
        this.okCancel = new OkCancelDialog();
        container.addChild(this.error);
        container.addChild(this.okCancel);
        this.okCancel.setCancelAction(new Action() {
            @Override
            public void perform() {
                NewMapDialog.this.destroy();
            }
        });
        this.okCancel.setOkAction(new Action() {
            @Override
            public void perform() {
                try {
//                    int size = Integer.parseInt(field.getValue());
//                    MapEditor.this.mapSize = size;
                    NewMapDialog.this.destroy();
                } catch (NumberFormatException e) {
                    showError("Error: bad number format");
                }
            }
        });
        // field
        field = new AoeField();
        field.setPosition(new Point(0, okCancel.getDimension().height));
        container.addChild(field);
        // text
        BitmapString text = new BitmapString(font, "Please enter map size");
        text.setPosition(new Point(0, okCancel.getDimension().height
                + field.getDimension().height));
        container.addChild(text);
        // container
        this.center(container);
    }

    private void destroy() {
        this.field.setValue("");
        this.hide();
        this.parent.unbindControllable(this.field);
        this.parent.unbindControllable(this.okCancel);
        this.hideError();
    }


    private void showError(String error) {
        this.error.setContent(error);
    }

    private void hideError() {
        this.error.setContent("");
    }

    public void setup() {
        this.show();
        this.parent.registerControllable(okCancel);
        this.parent.registerControllable(this.field);
    }
}