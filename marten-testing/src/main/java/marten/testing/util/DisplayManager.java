package marten.testing.util;

import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class DisplayManager {
	private static final int initWidth = 800;
	private static final int initHeight = 600;
	private static boolean fullscreen = false;
	private static GraphicsDevice dev =
		GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	private static DisplayMode origMode = dev.getDisplayMode();

	public static void setupDisplay(Frame frame, boolean tryFullscreen) {
		DisplayMode newMode = null;

		int width = initWidth;
		int height = initHeight;

		if (tryFullscreen) {
			if (dev.isFullScreenSupported()) {
				newMode = ScreenResSelector.showSelectionDialog();
				if (newMode != null) {
					width = newMode.getWidth();
					height = newMode.getHeight();
				}
			} else {
				System.err.println("NOTE: full-screen mode not supported; running in window instead");
			}
		}

		if (newMode != null) {
			frame.setUndecorated(true);
		}

		// this fullscreen workaround is for windows so we don't care
		// canvas.addGLEventListener(new FullscreenWorkaround(initWidth, initHeight));
		frame.setSize(width, height);
		frame.setVisible(true);

		if (dev.isFullScreenSupported() && (newMode != null) && tryFullscreen) {
			dev.setFullScreenWindow(frame);
			if (dev.isDisplayChangeSupported()) {
				dev.setDisplayMode(newMode);
				fullscreen = true;
			} else {
				// Not much point in having a full-screen window in this case
				dev.setFullScreenWindow(null);
				final Frame f2 = frame;
				try {
					EventQueue.invokeAndWait(new Runnable() {
						public void run() {
							f2.setVisible(false);
							f2.setUndecorated(false);
							f2.setVisible(true);
							f2.setSize(initWidth, initHeight);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.err.println("NOTE: was not able to change display mode; full-screen disabled");
			}
		}
	}

	public static void restoreOriginalDisplay() {
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					if (fullscreen) {
						try {
							dev.setDisplayMode(origMode);
						} catch (Exception e1) {
						}
						try {
							dev.setFullScreenWindow(null);
						} catch (Exception e2) {
						}
						fullscreen = false;
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
