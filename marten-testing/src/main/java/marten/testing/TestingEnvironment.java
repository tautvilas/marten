package marten.testing;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;

import marten.age.core.AgeApp;
import marten.age.util.SelectionDialog;
import marten.testing.util.DisplayManager;

import com.sun.opengl.util.Animator;

public class TestingEnvironment {
    // jogl animator
    private Animator animator;

    public TestingEnvironment() {
        Object instance = selectTest();

        if (instance == null)
            System.exit(0);

        if (instance instanceof AgeApp) {
            ((AgeApp) instance).execute();
            System.exit(0);
        } else if (instance instanceof ConsoleTest) {
            ((ConsoleTest) instance).run();
            System.exit(0);
        } else {
            Frame frame = new Frame("Aeon :: Simple testing environment");
            Animator animator;
            Component mainCanvas = null;
            if (instance instanceof GLEventListener) {
                final GLCanvas canvas = new GLCanvas();
                GLEventListener listener = (GLEventListener) instance;
                canvas.addGLEventListener(listener);
                animator = new Animator(canvas);
                animator.start();
                mainCanvas = canvas;
            } else {
                throw new RuntimeException("Unsupported test type");
            }

            if (mainCanvas == null)
                System.exit(0);
            frame.add(mainCanvas);

            DisplayManager.setupDisplay(frame, true);

            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    exit();
                }
            });

            mainCanvas.addKeyListener(new KeyListener() {
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        System.out.println("ESC pressed. Exiting...");
                        exit();
                    }
                }

                public void keyTyped(KeyEvent e) {
                }

                public void keyPressed(KeyEvent e) {
                }
            });
        }
    }

    private Object selectTest() {
        File dir = new File(Constants.TESTS_PATH);
        String[] children = dir.list();

        ArrayList<String> classNames = new ArrayList<String>();
        for (int i = 0; i < children.length; i++) {
            String[] tokens = children[i].split("\\.");
            // System.out.println(tokens[0]);
            if (tokens.length == 2 && tokens[1].equals("java")) {
                classNames.add(tokens[0]);
            }
        }

        String[] ids = new String[classNames.size()];
        for (int i = 0; i < classNames.size(); i++) {
            ids[i] = classNames.get(i);
        }

        SelectionDialog dialog = new SelectionDialog("Test selection", ids, 400);
        dialog.setVisible(true);
        dialog.waitFor();
        if (dialog.cancelWasPressed())
            return null;
        String classname = ids[dialog.selectedIndex()];

        Class<?> clas = null;
        Object instance = null;
        try {
            clas = Class.forName(Constants.TESTS_PACKAGE + "." + classname);
            instance = clas.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load test class " + classname);
        }

        return instance;
    }

    private void exit() {
        new Thread(new Runnable() {
            public void run() {
                if (animator != null)
                    animator.stop();
                DisplayManager.restoreOriginalDisplay();
                System.exit(0);
            }
        }).start();
    }

    public static void main(String args[]) {
        new TestingEnvironment();
    }
}
