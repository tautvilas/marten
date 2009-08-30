package marten.testing.tests;

import java.io.IOException;

import marten.age.camera.Camera;
import marten.age.camera.FrustumCamera;
import marten.age.core.AgeApp;
import marten.age.core.AgeScene;
import marten.age.event.AgeEvent;
import marten.age.flat.Hud;
import marten.age.flat.Sprite;
import marten.age.flat.TexturedSprite;
import marten.age.geometry.OptimizedGeometry;
import marten.age.geometry.SimpleModel;
import marten.age.geometry.primitives.Sphere;
import marten.age.image.ImageData;
import marten.age.root.SimpleRoot;
import marten.age.texture.Texture;
import marten.age.texture.TextureLoader;
import marten.age.util.FpsCounter;
import marten.util.Point;
import marten.util.Rotation;
import marten.util.Vector;

public class SpriteTest extends AgeApp implements AgeScene {

    @Override
    public void finalize() {
    }

    private SimpleRoot sr;
    private Camera mainCamera;

    @Override
    public void init() {
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

        ImageData data = null;
        try {
            data = new ImageData("data/textures/sprite.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Texture spriteTexture = TextureLoader.loadTexture(data);
        TexturedSprite sprite = new TexturedSprite(spriteTexture, new Point(0, 0));
        Sprite sp = new Sprite(data);
        sp.setPosition(new Point(10, 0));
        hud.addChild(sprite);
        hud.addChild(sp);

        /* Simple model */

        SimpleModel sm = new SimpleModel(new OptimizedGeometry(new Sphere(2.0)));
        sm.getAppearance().setTexture(spriteTexture);
        sr.addChild(sm);

//        sr.compile();
    }

    @Override
    public void compute() {
        sr.render();
    }

    @Override
    public void cleanup() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handle(AgeEvent e) {
    }

    @Override
    public void render() {
    }

    @Override
    public void configure() {
        this.setActiveScene(this);
    }
}
