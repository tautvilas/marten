package marten.aoe.gui.scene;

import java.awt.Font;

import marten.age.control.KeyboardController;
import marten.age.control.MouseController;
import marten.age.core.AgeScene;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.image.ImageCache;
import marten.age.graphics.image.ImageData;
import marten.age.widget.AgeField;
import marten.aoe.Path;

import org.apache.log4j.Logger;

public class JoinDialog extends AgeScene {

    @SuppressWarnings("unused")
    private static org.apache.log4j.Logger log = Logger
            .getLogger(JoinDialog.class);
    private Flatland flatland = new Flatland();

    public JoinDialog() {
        ImageData fieldImage = ImageCache
                .getImage(Path.SKIN_PATH + "field.png");
        ImageData cursorImage = ImageCache.getImage(Path.SKIN_PATH
                + "cursor.png");
        AgeField nickField = new AgeField(fieldImage, cursorImage, new Font(
                "Arial", Font.PLAIN, 20));
        this.flatland.addChild(nickField);
        this.flatland.compile();
        this.addController(new KeyboardController());
        this.addController(new MouseController());
        this.registerControllable(nickField);
    }

    @Override
    public void compute() {
    }

    @Override
    public void render() {
        flatland.render();
    }

}
