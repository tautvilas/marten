package marten.testing.visualization.jogl;

import java.awt.Font;
import java.util.HashMap;
import java.util.Set;

import com.sun.opengl.util.j2d.TextRenderer;

public class DebugBoxJogl extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;
	private int x, y;
	
	private TextRenderer renderer;

	public DebugBoxJogl(int x, int y) {
		renderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 10), true, true);
	}
	
	public void draw() {
		int tmpy = y;
		Set<String> keys = this.keySet();
		for (String key : keys) {
			renderer.beginRendering(800, 600);
			renderer.draw(key + ": " + this.get(key).toString(), x, tmpy);
			renderer.endRendering();
			tmpy += 10;
		}
	}
}