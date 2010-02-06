package marten.age.graphics.flat;

import marten.age.graphics.camera.Camera;
import marten.age.graphics.camera.FrustumCamera;
import marten.age.graphics.root.SimpleRoot;
import marten.util.Point;
import marten.util.Rotation;
import marten.util.Vector;

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

    public void addSprite(Sprite sprite, Point position) {
        sprite.setPosition(position);
        hud.addChild(sprite);
    }

    public void addSprite(Sprite sprite) {
        hud.addChild(sprite);
    }

    public void removeSprite(Sprite sprite) {
        hud.removeChild(sprite);
    }
}
