package marten.age.core;

import marten.age.graphics.primitives.Dimension;

import org.lwjgl.opengl.Display;

public class AppInfo {
    public static int getDisplayWidth() {
        return Display.getDisplayMode().getWidth();
    }

    public static int getDisplayHeight() {
        return Display.getDisplayMode().getHeight();
    }

    public static Dimension getDisplayDimension() {
        return new Dimension(Display.getDisplayMode().getWidth(), Display
                .getDisplayMode().getWidth());
    }
}
