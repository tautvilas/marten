package marten.age.core;

import java.io.IOException;
import java.nio.IntBuffer;

import marten.age.control.Controller;
import marten.age.event.AgeEvent;
import marten.age.event.AgeEventListener;
import marten.age.event.AgeSceneSwitchEvent;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.image.ImageTransformations;

import org.apache.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public abstract class AgeApp {
    private static org.apache.log4j.Logger log = Logger.getLogger(AgeApp.class);

    private AgeScene activeScene = null;

    // private Resource res = new FileSystemResource(Constants.UNIT_BEANS_PATH);
    // private BeanFactory factory = new XmlBeanFactory(res);

    private static final int DEFAULT_WIDHT = 1024;
    private static final int DEFAULT_HEIGHT = 800;

    private String title = "";
    private AgeScene sceneChanged = null;

    // private static final int FRAMERATE = 60;

    public int width = DEFAULT_WIDHT;
    public int height = DEFAULT_HEIGHT;

    public AgeApp() {
    }

    public AgeApp(String title) {
        this.title = title;
    }

    public void execute() {
        try {
            initDisplay();
        } catch (Exception le) {
            le.printStackTrace();
            log.fatal("Failed to initialize Age app");
            return;
        }

        log.info("GL_VENDOR: " + GL11.glGetString(GL11.GL_VENDOR));
        log.info("GL_RENDERER: " + GL11.glGetString(GL11.GL_RENDERER));
        log.info("GL_VERSION: " + GL11.glGetString(GL11.GL_VERSION));
        boolean FBOenabled = GLContext.getCapabilities().GL_EXT_framebuffer_object;
        if (!FBOenabled) {
            log.warn("Frame buffer object is not supported by your graphics adapter");
        }
        configure();
        runScene();
        destroy();
    }

    private void runScene() {
        if (this.activeScene == null) {
            log.error("No active scene provided for AGE. Exiting...");
            destroy();
        }

        activeScene.init();
        while (!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)
                && !Display.isCloseRequested()) {

            if (sceneChanged != null) {
                this.activeScene.cleanup();
                this.activeScene = sceneChanged;
                this.activeScene.init();
                sceneChanged = null;
            }

            Display.processMessages();

            this.activeScene.updateControllers();
            for (Controller c : activeScene.getControllers()) {
                c.publishEvents();
            }

            activeScene.compute();
            activeScene.render();

            Display.update(false);
            // Display.sync(FRAMERATE);
        }
    }

    protected void setActiveScene(AgeScene scene) {
        scene.registerListener(new CoreAgeEventListener());
        if (this.activeScene == null) {
            this.activeScene = scene;
        } else {
            this.sceneChanged = scene;
        }
    }

    protected void setCursor(String imagePath) {
        ImageData cursorImage = null;
        try {
            cursorImage = new ImageData(imagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cursorImage = ImageTransformations.flip(cursorImage);
        byte[] bytes = cursorImage.getBuffer();
        int[] ints = new int[bytes.length / 4];
        for (int i = 0; i < ints.length; i++) {
            int number = 0;
            number += (int)(bytes[i * 4 + 3] & 0xFF) << 24;
            number += (int)(bytes[i * 4] & 0xFF) << 16;
            number += (int)(bytes[i * 4 + 1] & 0xFF) << 8;
            number += (int)(bytes[i * 4 + 2] & 0xFF) << 0;
            ints[i] = number;
        }
        IntBuffer buffer = IntBuffer.wrap(ints);
        buffer.rewind();
        try {
            Mouse.setNativeCursor(new Cursor(16, 16, 0, 15, 1, buffer, null));
        } catch (LWJGLException e) {
            throw new RuntimeException(e);
        }
        // Mouse.setGrabbed(true);
    }

    private void destroy() {
        Display.destroy();
        finalize();
        log.info("Exiting normally...");
        System.exit(0);
    }

    private void initDisplay() throws Exception {
        DisplayMode mode = selectMode();
        boolean fullscreen = true;
        if (mode == null) {
            fullscreen = false;
            width = DEFAULT_WIDHT;
            height = DEFAULT_HEIGHT;
            mode = new DisplayMode(width, height);
        } else {
            width = mode.getWidth();
            height = mode.getHeight();
        }

        Display.setTitle(title);
        if (fullscreen) {
            Display.setDisplayModeAndFullscreen(mode);
        } else {
            Display.setDisplayMode(mode);
        }
        Display.setVSyncEnabled(true);
        Display.create();

    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    private DisplayMode selectMode() throws Exception {
        DisplayMode[] modes = Display.getAvailableDisplayModes();
        DisplayMode mode = null;
        SelectionDialog dialog = new SelectionDialog(
                "Select fullscreen display mode:", modesToString(modes));
        dialog.setVisible(true);
        dialog.waitFor();
        if (!dialog.cancelWasPressed()) {
            mode = modes[dialog.selectedIndex()];
        }
        return mode;
    }

    private String[] modesToString(DisplayMode[] modes) {
        String[] strings = new String[modes.length];
        int i = 0;
        for (DisplayMode mode : modes) {
            // NOTE:zvitruolis: very strange bug:
            // one could get similar output by
            // doing displayMode.toString(), but then program will crash in a
            // native method
            // the program crashes because bpp is included in string
            // we can include bpp in this string by mode.getBpp();
            // if we include bpp in this string the program crashes as well
            // bpp is int.
            // strange thing is that the program crashes only if there is
            // dialog.waitFor() method called in parent method, if
            // dialog.waitFor() is not called program does not crash.
            // must be some thread synchronization issue
            strings[i] = "" + mode.getWidth() + "x" + mode.getHeight() + "@"
                    + mode.getFrequency() + "Hz";
            i++;
        }
        return strings;
    }

    private class CoreAgeEventListener implements AgeEventListener {

        @Override
        public void handle(AgeEvent e) {
            if (e instanceof AgeSceneSwitchEvent) {
                AgeSceneSwitchEvent event = (AgeSceneSwitchEvent)e;
                log.debug("Switching AGE scene to " + event.newScene);
                setActiveScene(event.newScene);
            } else {
                log.warn("Unrecognized Age event " + e);
            }
        }

    }

    /*** Methods for overriding below ***/

    /* User has to override this method and do AGE configuration here */
    public abstract void configure();

    /* Do some final cleanup (overriding is optional) */
    public void finalize() {
    }
}
