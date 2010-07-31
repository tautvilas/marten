package marten.age.graphics.flat;

import marten.age.graphics.SceneGraphChild;
import marten.age.graphics.camera.FrustumCamera;
import marten.age.graphics.flat.sprite.PixelSprite;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.root.SimpleRoot;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.transform.TranslationGroup;

public class Flatland {

    private Hud hud;
    private SimpleRoot sr;
    private FrustumCamera mainCamera;

    public Flatland() {
        sr = new SimpleRoot();

        mainCamera = new FrustumCamera();
        sr.addCamera("front", mainCamera);
        sr.setActiveCamera("front");
        hud = new Hud();
        sr.addChild(hud);
    }

    public void render() {
        sr.render();
    }

    public void addChild(SceneGraphChild sprite) {
        hud.addChild(sprite);
    }

    public void removeChild(PixelSprite sprite) {
        hud.removeChild(sprite);
    }

    public void removeChildren() {
        hud.removeChildren();
    }

    public void addText(BitmapString text, Point position) {
        TranslationGroup translation = new TranslationGroup();
        translation.setCoordinates(position);
        translation.addChild(text);
        hud.addChild(translation);
    }
}
