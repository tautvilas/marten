package marten.aoe.gui.widget;

import marten.age.graphics.flat.TexturedSprite;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.primitives.Point;
import marten.age.widget.Widget;
import marten.aoe.engine.TileCoordinate;

public class TileWidget extends TexturedSprite implements Widget {

    public TileWidget(ImageData data, TileCoordinate position) {
        super(data);
        int imageWidth = data.width;
        int imageHeight = data.height;
        int delta = imageWidth + imageWidth / 2;
        if (position.x() % 2 == 0) {
            this.setPosition(new Point((position.x() / 2) * delta, position.y()
                    * imageHeight));
        } else {
            int deltax = imageHeight * 3 / 4;
            if (position.x() < 0) {
                deltax = -deltax;
            }
            this.setPosition(new Point((position.x() / 2) * delta
                    + deltax, position.y() * imageHeight
                    + imageHeight / 2));
        }
    }
}
