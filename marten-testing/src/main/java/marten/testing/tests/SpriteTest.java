package marten.testing.tests;

import java.io.IOException;

import marten.age.core.AgeApp;
import marten.age.core.AgeScene;
import marten.age.graphics.camera.Camera;
import marten.age.graphics.camera.FrustumCamera;
import marten.age.graphics.flat.Hud;
import marten.age.graphics.flat.sprite.PixelSprite;
import marten.age.graphics.flat.sprite.TextureSprite;
import marten.age.graphics.geometry.OptimizedGeometry;
import marten.age.graphics.geometry.primitives.Sphere;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.model.SimpleModel;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.primitives.Rotation;
import marten.age.graphics.primitives.Vector;
import marten.age.graphics.root.SimpleRoot;
import marten.age.graphics.texture.Texture;
import marten.age.graphics.texture.TextureLoader;
import marten.age.widget.obsolete.FpsCounter;

public class SpriteTest extends AgeApp {

    private class Scene extends AgeScene {

        private SimpleRoot sr;
        private Camera mainCamera;

        public Scene() {
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

            /* Pixel sprite */

            ImageData data = null;
            try {
                data = new ImageData("data/textures/sprite.png");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            PixelSprite sp = new PixelSprite(data);
            sp.setPosition(new Point(100, 0));
            hud.addChild(sp);

            /* Texture sprite */

            Texture spriteTexture = TextureLoader.loadTexture(data);
            TextureSprite sprite = new TextureSprite(spriteTexture, new Point(
                    0, 0));
            hud.addChild(sprite);

            /* Not opengl friendly sprite (width/height != x^2) */

            data = null;
            try {
                data = new ImageData("data/textures/sprite2.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
            spriteTexture = TextureLoader.loadTexture(data);
            sprite = new TextureSprite(spriteTexture, new Point(200, 0));
            hud.addChild(sprite);

            /* Simple model */

            SimpleModel sm = new SimpleModel(new OptimizedGeometry(new Sphere(
                    2.0)));
            sm.getAppearance().setTexture(spriteTexture);
            sr.addChild(sm);

            // sr.compile();
        }

        @Override
        public void compute() {
        }

        @Override
        public void cleanup() {
        }

        @Override
        public void render() {
            sr.render();
        }

    }

    @Override
    public void finalize() {
    }

    @Override
    public void configure() {
        this.setActiveScene(new Scene());
    }
}
