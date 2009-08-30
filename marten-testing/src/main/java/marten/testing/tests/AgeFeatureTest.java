package marten.testing.tests;

import marten.age.camera.Camera;
import marten.age.camera.FrustumCamera;
import marten.age.control.MouseController;
import marten.age.control.MouseListener;
import marten.age.core.AgeApp;
import marten.age.core.AgeScene;
import marten.age.event.AgeEvent;
import marten.age.flat.Hud;
import marten.age.geometry.OptimizedGeometry;
import marten.age.geometry.SimpleModel;
import marten.age.geometry.primitives.Sphere;
import marten.age.model.ComplexModel;
import marten.age.model.ModelLoader;
import marten.age.root.SimpleRoot;
import marten.age.text.BitmapFont;
import marten.age.text.BitmapString;
import marten.age.text.FontCache;
import marten.age.util.Color;
import marten.age.util.DebugBox;
import marten.age.util.FpsCounter;
import marten.testing.util.Body;
import marten.util.Point;
import marten.util.Rotation;
import marten.util.Vector;

public class AgeFeatureTest extends AgeApp implements AgeScene {

    private SimpleRoot sr;

    // private static Logger log = Logger.getLogger(AgeFeatureTest.class);

    private boolean mouseIsDown = false;
    private Point mouseCoords = new Point();

    private Camera mainCamera;

    private double mainCameraDistance = -10;
    private double shipRotationY = 0;
    private double shipRotationX = Math.PI / 2;

    DebugBox box;

    private Body spaceship;

    @Override
    public void init() {
        /* Scene Init */
        sr = new SimpleRoot();

        mainCamera = new FrustumCamera();
        Rotation r = new Rotation(new Vector());
        mainCamera.setSettings(new Point(), r, mainCameraDistance);
        mainCamera.setClippingPlanes(1.0, 1000.0);
        sr.addCamera("front", mainCamera);
        sr.setActiveCamera("front");

        /* Set up controllers */
        this.addController(new MouseController(new TestMouseListener()));

        /* Hud */

        Hud hud = new Hud();
        sr.addChild(hud);

        /* Bitmap Font */

        FontCache.generate();
        BitmapFont bfont = FontCache.getFont(FontCache.COURIER_BOLD_20);

        BitmapString string = new BitmapString(bfont, "AEON");
        string.setColor(new Color(1.0, 0.0, 0.0));

        hud.addChild(string);

        /* Debug box */

        box = new DebugBox();
        hud.addChild(box);

        /* FPS counter */

        hud.addChild(new FpsCounter());

        /* Simple model */

        SimpleModel sm = new SimpleModel(new OptimizedGeometry(new Sphere(2.0)));
        sr.addChild(sm);

        /* Model loading */

        ComplexModel model;
        try {
            model = ModelLoader
                    .load("data/models/obj/destroyerx/destroyerx.obj");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        spaceship = new Body(model);
        sr.addChild(spaceship.getSceneGraph());

        sr.compile();
    }

    @Override
    public void compute() {
        Rotation r = new Rotation();
        r.set(new Vector(0.0, 1.0, 0.0), shipRotationY);
        r = r.multiply(new Rotation(new Vector(1.0, 0.0, 0.0), shipRotationX));
        spaceship.setHeading(r);

        box.addObject("Ship heading", spaceship.getHeading());
        box.addObject("Ship position", spaceship.getPosition());

        sr.activate();
    }

    /* Mouse lister for mouse events catching */
    private class TestMouseListener implements MouseListener {
        public void mouseDown(Point coords) {
            mouseIsDown = true;
        }

        public void mouseMove(Point coords) {
            if (mouseIsDown) {
                double dx = mouseCoords.x - coords.x;
                double dy = mouseCoords.y - coords.y;
                if (dx > 0) {
                    shipRotationY = (shipRotationY - 0.05) % (2 * Math.PI);
                } else {
                    shipRotationY = (shipRotationY + 0.05) % (2 * Math.PI);
                }
                // System.out.println(mouseCoords.y + ":" + coords.y);
                if (dy > 0) {
                    shipRotationX = (shipRotationX + 0.05) % (2 * Math.PI);
                } else {
                    shipRotationX = (shipRotationX - 0.05) % (2 * Math.PI);
                }
            }
            mouseCoords.x = coords.x;
            mouseCoords.y = coords.y;
        }

        public void mouseUp(Point coords) {
            mouseIsDown = false;
        }

        /* Camera distance is being controlled by mouse wheel */
        public void mouseWheelRoll(int delta) {
            if (delta < 0) {
                mainCameraDistance += 1;
            } else {
                mainCameraDistance -= 1;
            }
        }
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void handle(AgeEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void render() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void configure() {
        this.setActiveScene(this);
    }

    @Override
    public void finalize() {
        // TODO Auto-generated method stub
        
    }
}
