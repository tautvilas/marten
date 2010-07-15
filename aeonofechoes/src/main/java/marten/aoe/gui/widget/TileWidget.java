package marten.aoe.gui.widget;

import marten.age.graphics.flat.Sprite;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.primitives.Point;
import marten.age.widget.Widget;
import marten.aoe.engine.TileCoordinate;

public class TileWidget extends Sprite implements Widget {

    public TileWidget(ImageData data, TileCoordinate position) {
        super(data);
        int imageWidth = data.width;
        int imageHeight = data.height;
        if (position.x() % 2 == 0) {
            this.setPosition(new Point(position.x() / 2
                    * (imageWidth + imageWidth / 2), position.y()
                    * imageHeight));
        } else {
            this.setPosition(new Point(position.x() * imageWidth / 2
                    + imageWidth * (position.x() - 2) + imageWidth / 4,
                    position.y() * imageHeight + imageHeight / 2));
        }
    }
}
