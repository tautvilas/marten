package marten.age.graphics.util;

import marten.age.graphics.BasicSceneGraphChild;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;

public class FpsCounter extends BasicSceneGraphChild {

	private BitmapFont font = FontCache.getFont(FontCache.COURIER_BOLD_20);

	private long startTime = 0;
	private long fps = 0;
	private long fpsOut = 0;
	
	private int interval = 2000;

	public void render() {
		if (startTime == 0) {
			startTime = System.currentTimeMillis() + interval;
		}

		if (startTime > System.currentTimeMillis()) {
			fps++;
		} else {
			long timeUsed = interval + (startTime - System.currentTimeMillis());
			startTime = System.currentTimeMillis() + interval;
			fpsOut = (long) (fps / (timeUsed / 1000f));
			fps = 0;
		}
		
		BitmapString string = new BitmapString(font, "" + fpsOut + "FPS");
		string.render();
	}
}
