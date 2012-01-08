package marten.aoe.gui.scene;

import java.io.File;
import java.util.ArrayList;

import marten.age.core.AgeScene;
import marten.age.graphics.image.ImageCache;
import marten.aoe.gui.TileImageFactory;

public abstract class TileLoader extends AgeScene {

    protected void loadLayers(ArrayList<String> priorities, String path) {
        File tileFolder = new File(path);
        File[] files = tileFolder.listFiles();
        for (int i = 0; i < files.length; i++) {
            String filename = files[i].getName();
            if (filename.charAt(0) == '.') continue;
            String[] parts = filename.split("_");
            String priority = parts[0];
            String newPath = path + "/" + filename;
            priorities.add(priority);
            if (files[i].isDirectory()) {
                this.loadLayers(priorities, newPath);
            } else {
                String[] priors = new String[priorities.size()];
                priorities.toArray(priors);
                TileImageFactory.addLayer(priors, parts[1].split("\\.")[0], newPath);
            }
            priorities.remove(priorities.size() - 1);
        }
    }

    protected void loadMasks(String path) {
        File tileFolder = new File(path);
        File[] files = tileFolder.listFiles();
        for (int i = 0; i < files.length; i++) {
            String filename = files[i].getName();
            if (filename.charAt(0) == '.') continue;
            ImageCache.loadImage(files[i].getAbsolutePath(), "mask-" + filename.split("\\.")[0]);
        }
    }
}
