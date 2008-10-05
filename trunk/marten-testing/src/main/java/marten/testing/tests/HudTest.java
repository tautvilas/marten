package marten.testing.tests;

import marten.age.AgeApp;
import marten.age.BitmapFont;
import marten.age.BitmapString;
import marten.age.Camera;
import marten.age.FontCache;
import marten.age.FrustumCamera;
import marten.age.Hud;
import marten.age.SimpleModel;
import marten.age.SimpleRoot;
import marten.age.geometry.OptimizedGeometry;
import marten.age.geometry.SphereGeometry;
import marten.age.util.Color;
import marten.age.util.DebugBox;
import marten.age.util.FpsCounter;
import marten.util.Point;
import marten.util.Rotation;

import org.lwjgl.opengl.Display;


public class HudTest extends AgeApp {
	
	private SimpleRoot sr;
	
	@Override
	protected void init() {
		sr = new SimpleRoot();
		Hud hud = new Hud();
		
		SimpleModel sm = new SimpleModel(new OptimizedGeometry(new SphereGeometry(2.0)));
		
		Camera frontCamera = new FrustumCamera();
		frontCamera.setSettings(new Point(), new Rotation(), -10.0);
		sr.addCamera("front", frontCamera);
		sr.setActiveCamera("front");
		sr.addHud("mainhud", hud);
		sr.setActiveHud("mainhud");
		
		FontCache.generate();
		BitmapFont bfont = FontCache.getFont(FontCache.COURIER_BOLD_20);
		
		BitmapString string = new BitmapString(bfont, "AEON");
		string.setColor(new Color(1.0, 0.0, 0.0));
		hud.add(string, 400, 300);
//		string = new BitmapString(bfont, "Corner case");
		DebugBox box = new DebugBox();
		box.addObject("Speed", new Integer(200));
		box.addObject("Something", new Point());
		hud.add(box, 0, 0);
		hud.add(new FpsCounter(), Display.getDisplayMode().getWidth() - 100, 0);
		sr.addBranch(sm);
	}

	public void keyDown(int key) {
		
	}

	public void keyUp(int key) {
	}

	protected void mainLoop() {
		sr.activate();
	}

	public void mouseDown(Point coords) {
	}

	public void mouseMove(Point coords) {
	}

	public void mouseUp(Point coords) {
	}
}
