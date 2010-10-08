package marten.age.graphics.image;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class ImageCache {
    private static org.apache.log4j.Logger log = Logger.getLogger(ImageCache.class);
    private static HashMap<String, ImageData> images = new HashMap<String, ImageData>();

    public static void loadImage(String path) {
        if (!images.containsKey(path)) {
            ImageData image = ImageLoader.loadImage(path);
            images.put(path, image);
            log.debug("Image " + path + " added to image cache.");
        } else {
            log.debug("Image " + path + " is allready in font cache.");
        }
    }

    public static ImageData getImage(String path) {
        if (!images.containsKey(path)) {
            loadImage(path);
        } else {
            log.debug("Found image " + path + " in image cache. Returning.");
        }
        return images.get(path);
    }
}
