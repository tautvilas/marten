package marten.aoe.gui.scene;

import marten.age.core.AgeScene;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.flat.sprite.TextureSprite;
import marten.age.graphics.image.ImageCache;
import marten.age.graphics.image.ImageData;
import marten.aoe.Path;

import org.apache.log4j.Logger;

public class JoinDialog extends AgeScene {

    @SuppressWarnings("unused")
    private static org.apache.log4j.Logger log = Logger.getLogger(JoinDialog.class);
    private Flatland flatland = new Flatland();

    public JoinDialog() {
        // load graphics
        ImageData menuButtonImage = ImageCache.getImage(Path.SKIN_PATH + "menu-button.png");
        this.flatland.addChild(new TextureSprite(menuButtonImage));
        this.flatland.compile();
    }

    @Override
    public void compute() {
    }

    @Override
    public void render() {
        flatland.render();
    }

}
