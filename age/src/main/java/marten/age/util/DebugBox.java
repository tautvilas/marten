package marten.age.util;

import java.util.HashMap;
import java.util.Set;

import marten.age.SceneGraphChild;
import marten.age.text.BitmapFont;
import marten.age.text.BitmapString;
import marten.age.text.FontCache;

import org.lwjgl.opengl.GL11;


public class DebugBox implements SceneGraphChild {
	
	private HashMap<String, Object> objects = new HashMap<String, Object>();

	private static final long serialVersionUID = 1L;
	
	private BitmapFont font = FontCache.getFont(FontCache.COURIER_14);
	
	public void addObject(String key, Object o) {
		objects.put(key, o);
	}
	
	public void removeObject(String key) {
		objects.remove(key);
	}
	
	public void activate() {
		GL11.glPushMatrix();
		Set<String> keys = objects.keySet();
		for (String key : keys) {
			Object o = objects.get(key);
			BitmapString string = new BitmapString(font, key + ": " + o.toString());
			string.activate();
			GL11.glTranslated(0.0, font.getSize(), 0.0);
		}
		GL11.glPopMatrix();
	}
}