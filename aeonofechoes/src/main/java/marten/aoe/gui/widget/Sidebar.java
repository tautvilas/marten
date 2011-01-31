package marten.aoe.gui.widget;

import marten.age.graphics.appearance.Appearance;
import marten.age.graphics.flat.SimpleLayout;
import marten.age.graphics.geometry.Geometry;
import marten.age.graphics.geometry.primitives.Rectangle;
import marten.age.graphics.image.ImageCache;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.model.SimpleModel;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.texture.TextureLoader;
import marten.aoe.Path;

public class Sidebar extends SimpleLayout {

    public Sidebar(Dimension dimension) {
        super(dimension);
        ImageData sidebarImage = ImageCache.getImage(Path.SKIN_DATA_PATH
                + "sidebar.png");
        SimpleModel model = new SimpleModel(new Appearance(TextureLoader
                .loadTexture(sidebarImage)));
        for (int i = 0; i < dimension.height; i += sidebarImage.height) {
            Geometry rectangle = new Rectangle(new Dimension(sidebarImage.width, sidebarImage.height), new Point(0, i));
            model.addGeometry(rectangle);
        }
        this.addChild(model);
    }
}
