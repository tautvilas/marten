package marten.aoe.gui.scene;

import java.io.File;
import java.util.ArrayList;

import marten.age.core.AgeScene;
import marten.age.graphics.image.ImageCache;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.image.ImageTransformations;
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
        ImageData mask1_1 = ImageCache.getImage("mask-1-1");
        ImageData mask1_2 = ImageCache.getImage("mask-1-2");
        ImageCache.addImage("msk-1-0", mask1_1);
        ImageCache.addImage("msk-1-1", mask1_2);
        ImageCache.addImage("msk-1-2", ImageTransformations.flip(mask1_2));
        ImageCache.addImage("msk-1-3", ImageTransformations.flip(mask1_1));
        ImageCache.addImage("msk-1-4", ImageTransformations.rotate(mask1_2, 2));
        ImageCache.addImage("msk-1-5", ImageTransformations.vflip(mask1_2));
    }
}
