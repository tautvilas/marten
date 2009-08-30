package marten.age.util;

import java.util.HashSet;

import marten.age.control.Controller;
import marten.age.event.AgeEvent;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public abstract class AgeApp {
    private static org.apache.log4j.Logger log = Logger.getLogger(AgeApp.class);

    private HashSet<Controller> controllers = new HashSet<Controller>();

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
            init();
        } catch (Exception le) {
            le.printStackTrace();
            log.fatal("Failed to initialize Age app");
            return;
        }

        while (!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)
                && !Display.isCloseRequested()) {
            Display.update();

            // publish events to controller listeners
            for (Controller c : controllers) {
                c.publishEvents();
            }

            compute();
            Display.sync(FRAMERATE);
        }

        log.info("Exiting...");

        destroy();
    }

    public void addController(Controller c) {
        controllers.add(c);
    }

    public void removeController(Controller c) {
        controllers.remove(c);
    }

    private void destroy() {
        Display.destroy();
        Keyboard.destroy();
        cleanup();
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
            //if we include bpp in this string the program crashes as well
            //bpp is int.
            //strange thing is that the program crashes only if there is
            //dialog.waitFor() method called in parent method, if
            //dialog.waitFor() is not called program does not crash.
            //must be some thread synchronization issue
            strings[i] = "" + mode.getWidth() + "x" + mode.getHeight() + "@"
                    + mode.getFrequency() + "Hz";
            i++;
        }
        return strings;
    }

    protected abstract void init();

    protected abstract void handle(AgeEvent e);

    protected abstract void compute();

    protected abstract void render();

    protected abstract void cleanup();
}
