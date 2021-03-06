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
import marten.age.graphics.image.ImageTransformations;
import marten.age.graphics.model.SimpleModel;
import marten.age.graphics.primitives.Dimension;
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
            sp.setPosition(new Point(0, 100));
            hud.addChild(sp);

            /* Texture sprite */

            Texture spriteTexture = TextureLoader.loadTexture(data);
            TextureSprite sprite = new TextureSprite(spriteTexture, new Point(
                    100, 100));
            hud.addChild(sprite);

            /* Not opengl friendly sprite (width/height != x^2) */

            data = null;
            try {
                data = new ImageData("data/textures/sprite2.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
            spriteTexture = TextureLoader.loadTexture(data);
            sprite = new TextureSprite(spriteTexture, new Point(200, 100));
            hud.addChild(sprite);

            /* Test image blend with mask */

            data = null;
            ImageData top = null;
            ImageData mask = null;
            try {
                data = new ImageData("data/textures/sprite-base.png");
                top= new ImageData("data/textures/sprite-top.png");
                mask= new ImageData("data/textures/sprite-mask.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
            data = ImageTransformations.blend(data, top, mask);
            spriteTexture = TextureLoader.loadTexture(data);
            sprite = new TextureSprite(spriteTexture, new Point(300, 100));
            hud.addChild(sprite);

            /* Select only part of image as texture */

            data = null;
            try {
                data = new ImageData("data/textures/sprite3.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
            spriteTexture = TextureLoader.loadTexture(data);
            sprite = new TextureSprite(spriteTexture, new Point(200 - 7, 300));
            hud.addChild(sprite);
            spriteTexture = spriteTexture.crop(new Point(7, 21), new Dimension(
                    30, 26));
            sprite = new TextureSprite(spriteTexture, new Point(200, 200));
            hud.addChild(sprite);


            /* Rotate sprite */
            ImageData data2 = ImageTransformations.rotate(data);
            spriteTexture = TextureLoader.loadTexture(data2);
            sprite = new TextureSprite(spriteTexture, new Point(500, 300));
            hud.addChild(sprite);
            
            data2 = ImageTransformations.rotate(data2);
            spriteTexture = TextureLoader.loadTexture(data2);
            sprite = new TextureSprite(spriteTexture, new Point(600, 300));
            hud.addChild(sprite);
            
            data2 = ImageTransformations.rotate(data2);
            spriteTexture = TextureLoader.loadTexture(data2);
            sprite = new TextureSprite(spriteTexture, new Point(750, 300));
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
