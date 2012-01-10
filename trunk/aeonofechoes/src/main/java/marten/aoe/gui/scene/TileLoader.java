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
        ImageData mask2_1 = ImageCache.getImage("mask-2-1");
        ImageData mask2_2 = ImageCache.getImage("mask-2-2");
        ImageCache.addImage("msk-2-0", mask2_2);
        ImageCache.addImage("msk-2-1", ImageTransformations.vflip(mask2_2));
        ImageCache.addImage("msk-2-2", ImageTransformations.vflip(mask2_1));
        ImageCache.addImage("msk-2-3", ImageTransformations.rotate(mask2_2, 2));
        ImageCache.addImage("msk-2-4", ImageTransformations.flip(mask2_2));
        ImageCache.addImage("msk-2-5", mask2_1);
        ImageData mask3_1 = ImageCache.getImage("mask-3-1");
        ImageData mask3_2 = ImageCache.getImage("mask-3-2");
        ImageCache.addImage("msk-3-0", ImageTransformations.rotate(mask3_2, 2));
        ImageCache.addImage("msk-3-1", ImageTransformations.flip(mask3_1));
        ImageCache.addImage("msk-3-2", ImageTransformations.flip(mask3_2));
        ImageCache.addImage("msk-3-3", mask3_2);
        ImageCache.addImage("msk-3-4", mask3_1);
        ImageCache.addImage("msk-3-5", ImageTransformations.vflip(mask3_2));
        ImageData mask6 = ImageCache.getImage("mask-6");
        ImageData msk_2_0 = ImageCache.getImage("msk-2-0");
        ImageData msk_2_1 = ImageCache.getImage("msk-2-1");
        ImageData msk_2_2 = ImageCache.getImage("msk-2-2");
        ImageData msk_2_3 = ImageCache.getImage("msk-2-3");
        ImageData msk_2_4 = ImageCache.getImage("msk-2-4");
        ImageData msk_2_5 = ImageCache.getImage("msk-2-5");
        ImageCache.addImage("msk-4-0", ImageTransformations.blend(msk_2_0, msk_2_4));
        ImageCache.addImage("msk-4-1", ImageTransformations.blend(msk_2_1, msk_2_5));
        ImageCache.addImage("msk-4-2", ImageTransformations.blend(msk_2_2, msk_2_0));
        ImageCache.addImage("msk-4-3", ImageTransformations.blend(msk_2_3, msk_2_1));
        ImageCache.addImage("msk-4-4", ImageTransformations.blend(msk_2_4, msk_2_2));
        ImageCache.addImage("msk-4-5", ImageTransformations.blend(msk_2_5, msk_2_3));
        ImageCache.addImage("msk-6", mask6);
    }
}
