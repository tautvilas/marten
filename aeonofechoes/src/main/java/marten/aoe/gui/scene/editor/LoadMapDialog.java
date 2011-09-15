package marten.aoe.gui.scene.editor;

import marten.age.core.AgeScene;
import marten.age.graphics.layout.BoxedObject;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.widget.Action;
import marten.aoe.gui.widget.AoeField;
import marten.aoe.gui.widget.Dialog;
import marten.aoe.gui.widget.OkCancelDialog;

public class LoadMapDialog extends Dialog implements BoxedObject {

    private String mapName = "";

    public LoadMapDialog(AgeScene parent, final Action loadMap) {
        super(new Dimension(500, 300));
        final AoeField field = new AoeField();
        this.hide();
        OkCancelDialog okCancel = new OkCancelDialog();
        okCancel.setCancelAction(new Action() {
            @Override
            public void perform() {
                LoadMapDialog.this.hide();
            }
        });
        okCancel.setOkAction(new Action() {
            @Override
            public void perform() {
                LoadMapDialog.this.mapName = field.getValue();
                loadMap.perform();
            }
        });
        this.addChild(okCancel);
        field.setPosition(new Point(10, 100));
        this.addChild(field);
        parent.registerControllable(okCancel);
        parent.registerControllable(field);
    }

    public String getMapName() {
        return this.mapName;
    }
}
