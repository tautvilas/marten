package marten.testing.tests;

import marten.age.camera.Camera;
import marten.age.camera.FrustumCamera;
import marten.age.control.MouseController;
import marten.age.control.MouseListener;
import marten.age.geometry.OptimizedGeometry;
import marten.age.geometry.SimpleModel;
import marten.age.geometry.SphereGeometry;
import marten.age.hud.Hud;
import marten.age.model.ComplexModel;
import marten.age.model.ModelLoader;
import marten.age.root.SimpleRoot;
import marten.age.text.BitmapFont;
import marten.age.text.BitmapString;
import marten.age.text.FontCache;
import marten.age.util.AgeApp;
import marten.age.util.Color;
import marten.age.util.DebugBox;
import marten.age.util.FpsCounter;
import marten.util.Point;
import marten.util.Rotation;
import marten.util.Vector;

import org.lwjgl.opengl.Display;

public class AgeFeatureTest extends AgeApp {
	
	private SimpleRoot sr;
	
//	private static Logger log = Logger.getLogger(AgeFeatureTest.class);
	
	private boolean mouseIsDown = false;
	private Point mouseCoords = new Point();
	
	private Camera mainCamera;
	
	// TODO: Rotation methods : setXRotationAngle, Y, Z, get, setXYZroation(x, y, z)
	private double mainCameraDistance = -10;
	private double mainCameraRotationY = 0;
	private double mainCameraRotationX = 0;

	@Override
	protected void init() {
		/* Scene Init */
		sr = new SimpleRoot();
		
		mainCamera = new FrustumCamera();
		Rotation r = new Rotation();
		r.set(new Vector(0.0, 1.0, 0.0), mainCameraRotationY);
		r.set(new Vector(1.0, 0.0, 0.0), mainCameraRotationX);
		mainCamera.setSettings(new Point(), r, mainCameraDistance);
		mainCamera.setClippingPlanes(1.0, 1000.0);
		sr.addCamera("front", mainCamera);
		sr.setActiveCamera("front");
		
		/* Set up controllers */
		this.addController(new MouseController(new TestMouseListener()));
		
		/* Hud */
		
		Hud hud = new Hud();
		sr.addHud("mainhud", hud);
		sr.setActiveHud("mainhud");
		
		/* Bitmap Font */
		
		FontCache.generate();
		BitmapFont bfont = FontCache.getFont(FontCache.COURIER_BOLD_20);
		
		BitmapString string = new BitmapString(bfont, "AEON");
		string.setColor(new Color(1.0, 0.0, 0.0));
		
		hud.add(string, 400, 300);
		
		/* Debug box */

		DebugBox box = new DebugBox();
		box.addObject("Speed", new Integer(200));
		box.addObject("Something", new Point());
		hud.add(box, 0, 0);
		
		/* FPS counter */
		
		hud.add(new FpsCounter(), Display.getDisplayMode().getWidth() - 100, 0);
		
		/* Simple model */
		
		SimpleModel sm = new SimpleModel(new OptimizedGeometry(new SphereGeometry(2.0)));
		sr.addBranch(sm);
		
		/* Model loading*/
		
		try {
			ComplexModel model = ModelLoader.load("data/models/obj/destroyerx/destroyerx.obj");
			sr.addBranch(model);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void mainLoop() {
		sr.activate();
	}
	
	/* Mouse lister for mouse events catching */
	private class TestMouseListener implements MouseListener {
		public void mouseDown(Point coords) {
			mouseIsDown = true;
			mouseCoords = coords;
		}
	
		public void mouseMove(Point coords) {
			if (mouseIsDown) {
				double dx = mouseCoords.x - coords.x;
				double dy = mouseCoords.y - coords.y;
				if (dx > 0) {
					mainCameraRotationY= (mainCameraRotationY + 0.2) % 360;
				} else {
					mainCameraRotationY= (mainCameraRotationY - 0.2) % 360;
				}
				if (dy > 0) {
					mainCameraRotationX= (mainCameraRotationX + 0.2) % 360;
				} else {
					mainCameraRotationX= (mainCameraRotationX - 0.2) % 360;
				}
				Rotation r = new Rotation();
				r.set(new Vector(0.0, 1.0, 0.0), mainCameraRotationY);
				r.set(new Vector(1.0, 0.0, 0.0), mainCameraRotationX);
				mainCamera.setSettings(new Point(), r, mainCameraDistance);
			}
		}
	
		public void mouseUp(Point coords) {
			mouseIsDown = false;
			mouseCoords = coords;
		}
	
		/* Camera distance is being controlled by mouse wheel */
		public void mouseWheelRoll(int delta) {
			if (delta < 0) {
				mainCameraDistance += 1;
			} else {
				mainCameraDistance -= 1;
			}
			Rotation r = new Rotation();
			r.set(new Vector(0.0, 1.0, 0.0), mainCameraRotationY);
			r.set(new Vector(1.0, 0.0, 0.0), mainCameraRotationX);
			mainCamera.setSettings(new Point(), r, mainCameraDistance);
		}
	}
}
