package marten.age.graphics.flat;

import marten.age.graphics.SceneGraphChild;
import marten.age.graphics.camera.Camera;
import marten.age.graphics.camera.FrustumCamera;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.primitives.Rotation;
import marten.age.graphics.primitives.Vector;
import marten.age.graphics.root.SimpleRoot;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.transform.TranslationGroup;

public class Flatland {

    private Hud hud;
    private SimpleRoot sr;

    public Flatland() {
        sr = new SimpleRoot();

        Camera mainCamera = new FrustumCamera();
        mainCamera.setSettings(new Point(), new Rotation(new Vector()), -10);
        mainCamera.setClippingPlanes(1.0, 1000.0);
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

    public void removeChild(Sprite sprite) {
        hud.removeChild(sprite);
    }

    public void addText(BitmapString text, Point position) {
        TranslationGroup translation = new TranslationGroup();
        translation.setCoordinates(position);
        translation.addChild(text);
        hud.addChild(translation);
    }
}
