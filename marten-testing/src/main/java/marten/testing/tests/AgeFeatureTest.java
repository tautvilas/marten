package marten.testing.tests;

import marten.age.AgeApp;
import marten.age.BitmapFont;
import marten.age.BitmapString;
import marten.age.Camera;
import marten.age.ComplexModel;
import marten.age.FontCache;
import marten.age.FrustumCamera;
import marten.age.Hud;
import marten.age.ModelLoader;
import marten.age.SimpleModel;
import marten.age.SimpleRoot;
import marten.age.control.MouseController;
import marten.age.control.MouseListener;
import marten.age.geometry.OptimizedGeometry;
import marten.age.geometry.SphereGeometry;
import marten.age.util.Color;
import marten.age.util.DebugBox;
import marten.age.util.FpsCounter;
import marten.util.Point;
import marten.util.Rotation;

import org.apache.log4j.Logger;
import org.lwjgl.opengl.Display;

public class AgeFeatureTest extends AgeApp implements MouseListener {
	
	private SimpleRoot sr;
	
	private static Logger log = Logger.getLogger(AgeFeatureTest.class);

	@Override
	protected void init() {
		/* Scene Init */
		sr = new SimpleRoot();
		
		Camera frontCamera = new FrustumCamera();
		frontCamera.setSettings(new Point(), new Rotation(), -10.0);
		sr.addCamera("front", frontCamera);
		sr.setActiveCamera("front");
		
		/* Set up controllers */
		this.addController(new MouseController(this));
		
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
			ModelLoader loader = new ModelLoader("data/models/obj/destroyerx/destroyerx.obj");
			ComplexModel model = loader.load();
			sr.addBranch(model);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void mainLoop() {
		sr.activate();
	}

	@Override
	public void mouseDown(Point coords) {
		log.info("Mouse down");
	}

	@Override
	public void mouseMove(Point coords) {
	}

	@Override
	public void mouseUp(Point coords) {
	}

	@Override
	public void mouseWheelRoll(int delta) {
	}
}
