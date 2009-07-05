package marten.testing.tests;

import marten.age.camera.Camera;
import marten.age.camera.FrustumCamera;
import marten.age.flat.Sprite;
import marten.age.flat.TexturedSprite;
import marten.age.geometry.OptimizedGeometry;
import marten.age.geometry.SimpleModel;
import marten.age.geometry.primitives.Sphere;
import marten.age.hud.Hud;
import marten.age.root.SimpleRoot;
import marten.age.texture.Texture;
import marten.age.texture.TextureLoader;
import marten.age.util.AgeApp;
import marten.age.util.FpsCounter;
import marten.util.Point;
import marten.util.Rotation;
import marten.util.Vector;

public class SpriteTest extends AgeApp {

    @Override
    protected void finalize() {
    }

    private SimpleRoot sr;
    private Camera mainCamera;

    @Override
    protected void init() {
        /* Scene Init */
        sr = new SimpleRoot();

        mainCamera = new FrustumCamera();
        Rotation r = new Rotation(new Vector());
        mainCamera.setSettings(new Point(), r, -10);
        mainCamera.setClippingPlanes(1.0, 1000.0);
        sr.addCamera("front", mainCamera);
        sr.setActiveCamera("front");

        /* Hud */

        Hud hud = new Hud();
        sr.addChild(hud);

        /* FPS counter */

        hud.addChild(new FpsCounter());

        /* Sprite */

        Texture spriteTexture = TextureLoader.loadTexture("data/textures/sprite.png");
        TexturedSprite sprite = new TexturedSprite(spriteTexture, new Point(0, 0));
        Sprite sp = new Sprite("data/textures/sprite.png");
        sp.setPosition(new Point(10, 10));
        hud.addChild(sp);
        hud.addChild(sprite);

        /* Simple model */

        SimpleModel sm = new SimpleModel(new OptimizedGeometry(new Sphere(2.0)));
        sm.getAppearance().setTexture(spriteTexture);
        sr.addChild(sm);

//        sr.compile();
    }

    @Override
    protected void mainLoop() {
        sr.activate();
    }
}
