package marten.aoe.gui.widget;

import marten.age.control.MouseListener;
import marten.age.graphics.image.ImageCache;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.layout.BoxedObject;
import marten.age.graphics.layout.SimpleLayout;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.widget.Action;
import marten.age.widget.Button;
import marten.aoe.Path;

public class OkCancelDialog extends SimpleLayout implements MouseListener, BoxedObject {

    private Button ok;
    private Button cancel;

    public OkCancelDialog() {
        this(new Dimension(300, 32));
    }

    public OkCancelDialog(Dimension dimension) {
        super(dimension);
        ImageData buttonImage = ImageCache.getImage(Path.SKIN_DATA_PATH
                + "editor-button.png");
        ok = new Button(buttonImage);
        ok.setLabel("OK");
        cancel = new Button(buttonImage);
        cancel.setLabel("Cancel");
        this.addToLeft(cancel);
        this.addToRight(ok);
    }

    public void setOkAction(Action action) {
        ok.setAction(action);
    }

    public void setCancelAction(Action action) {
        cancel.setAction(action);
    }

    @Override
    public void mouseDown(Point coords) {
        ok.mouseDown(coords);
        cancel.mouseDown(coords);
    }

    @Override
    public void mouseMove(Point coords) {
        ok.mouseMove(coords);
        cancel.mouseMove(coords);
    }

    @Override
    public void mouseUp(Point coords) {
        ok.mouseUp(coords);
        cancel.mouseUp(coords);
    }

    @Override
    public void mouseWheelRoll(int delta, Point coords) {
    }

}
