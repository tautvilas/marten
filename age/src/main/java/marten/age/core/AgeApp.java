package marten.age.core;

import marten.age.control.Controller;
import marten.age.event.AgeEvent;
import marten.age.event.AgeEventListener;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public abstract class AgeApp {
    private static org.apache.log4j.Logger log = Logger.getLogger(AgeApp.class);

    private AgeScene activeScene = null;

    // private Resource res = new FileSystemResource(Constants.UNIT_BEANS_PATH);
    // private BeanFactory factory = new XmlBeanFactory(res);

    private static final int DEFAULT_WIDHT = 800;
    private static final int DEFAULT_HEIGHT = 600;

    private String title = "";
    // private DisplayMode displayMode;

    private static final int FRAMERATE = 60;

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

        configure();
        runScene();
        destroy();
    }

    private void runScene() {
        if (this.activeScene == null) {
            log.error("No active scene provided for AGE. Exiting...");
            destroy();
        }

        while (!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)
                && !Display.isCloseRequested()) {
            Display.update();

            // publish events to controller listeners
            for (Controller c : activeScene.getControllers()) {
                c.publishEvents();
            }

            activeScene.compute();
            activeScene.render();
            Display.sync(FRAMERATE);
        }
    }

    protected void setActiveScene(AgeScene scene) {
        scene.registerListener(new CoreAgeEventListener());
        this.activeScene = scene;
    }

    protected void switchScene(AgeScene scene) {
        this.activeScene.cleanup();
        this.setActiveScene(scene);
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

        Display.setDisplayMode(mode);
        Display.setTitle(title);
        if (fullscreen) {
            Display.setFullscreen(true);
        }
        // Display.setVSyncEnabled(true);
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
            if (e.id == "SCENE SWITCH") {
                AgeScene scene = (AgeScene)e.parameters[0];
                switchScene(scene);
                log.info("AGE scene switced to " + scene);
            } else {
                log.warn("Unrecognized Age event id: " + e.id);
            }
        }

    }

    /*** Methods for overriding below ***/

    /* User has to override this method and do AGE configuration here */
    public abstract void configure();

    /* Do some final cleanup (overriding is optional) */
    public void finalize() {}
}
