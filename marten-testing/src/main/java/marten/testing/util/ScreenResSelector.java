package marten.testing.util;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;

import marten.age.core.SelectionDialog;

public class ScreenResSelector {
  static interface DisplayModeFilter {
    public boolean filter(DisplayMode mode);
  }

  public static java.util.List<DisplayMode> getAvailableDisplayModes() {
    java.util.List<DisplayMode> modes = getDisplayModes();
    final DisplayMode curMode = getCurrentDisplayMode();
    // Filter everything which is higher frequency than the current
    // display mode
    modes = filterDisplayModes(modes, new DisplayModeFilter() {
        public boolean filter(DisplayMode mode) {
          return (mode.getRefreshRate() <= curMode.getRefreshRate());
        }
      });
    // Filter everything that is not at least 24-bit
    modes = filterDisplayModes(modes, new DisplayModeFilter() {
        public boolean filter(DisplayMode mode) {
          // Bit depth < 0 means "multi-depth" -- can't reason about it
          return (mode.getBitDepth() < 0 || mode.getBitDepth() >= 24);
        }
      });
    // Filter everything less than 640x480
    modes = filterDisplayModes(modes, new DisplayModeFilter() {
        public boolean filter(DisplayMode mode) {
          return (mode.getWidth() >= 640 && mode.getHeight() >= 480);
        }
      });
    if (modes.size() == 0) {
      throw new RuntimeException("Couldn't find any valid display modes");
    }
    return modes;
  }

  /** Shows a modal dialog containing the available screen resolutions
      and requests selection of one of them. Returns the selected one. */
  public static DisplayMode showSelectionDialog() {
    List<DisplayMode> modes = getAvailableDisplayModes();
    SelectionDialog dialog = new SelectionDialog("", modesToString(modes));
    dialog.setVisible(true);
    dialog.waitFor();
    if (dialog.cancelWasPressed()) return null;
    int selection = dialog.selectedIndex();
    return modes.get(selection);
  }

  //----------------------------------------------------------------------
  // Internals only below this point
  //

  private static DisplayMode getCurrentDisplayMode() {
    GraphicsDevice dev = getDefaultScreenDevice();
    return dev.getDisplayMode();
  }

  private static java.util.List<DisplayMode> getDisplayModes() {
    GraphicsDevice dev = getDefaultScreenDevice();
    DisplayMode[] modes = dev.getDisplayModes();
    return toList(modes);
  }

  private static GraphicsDevice getDefaultScreenDevice() {
    return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
  }

  private static java.util.List<DisplayMode> toList(DisplayMode[] modes) {
    java.util.List<DisplayMode> res = new ArrayList<DisplayMode>();
    for (int i = 0; i < modes.length; i++) {
      res.add(modes[i]);
    }
    return res;
  }

  private static String modeToString(DisplayMode mode) {
    return (((mode.getBitDepth() > 0) ? (mode.getBitDepth() + " bits, ") : "") +
            mode.getWidth() + "x" + mode.getHeight() + ", " +
            mode.getRefreshRate() + " Hz");
  }

  private static String[] modesToString(java.util.List<DisplayMode> modes) {
    String[] res = new String[modes.size()];
    int i = 0;
    for (DisplayMode dm : modes) {
      res[i++] = modeToString((DisplayMode) dm);
    }
    return res;
  }

  private static java.util.List<DisplayMode> filterDisplayModes(java.util.List<DisplayMode> modes,
                                                                    DisplayModeFilter filter) {
    java.util.List<DisplayMode> res = new ArrayList<DisplayMode>();
    for (DisplayMode dm : modes) {
      DisplayMode mode = (DisplayMode) dm;
      if (filter.filter(mode)) {
        res.add(mode);
      }
    }
    return res;
  }
}
