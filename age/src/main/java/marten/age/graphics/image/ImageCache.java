package marten.age.graphics.image;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class ImageCache {
    @SuppressWarnings("unused")
    private static org.apache.log4j.Logger log = Logger
            .getLogger(ImageCache.class);
    private static HashMap<String, ImageData> images = new HashMap<String, ImageData>();

    public static void loadImage(String path, String cacheId) {
        if (!images.containsKey(cacheId)) {
            ImageData image = ImageLoader.loadImage(path);
            images.put(cacheId, image);
            // log.debug("IMAGE '" + cacheId + "' added to image cache as " + cacheId);
        } else {
            // log.debug("Image " + path + " is allready in font cache.");
        }
    }

    public static void loadImage(String path) {
        if (!images.containsKey(path)) {
            ImageData image = ImageLoader.loadImage(path);
            images.put(path, image);
            // log.debug("IMAGE " + path + " added to image cache.");
        } else {
            // log.debug("Image " + path + " is allready in font cache.");
        }
    }

    public static void addImage(String cacheId, ImageData data) {
        if (!images.containsKey(cacheId)) {
            images.put(cacheId, data);
            // log.debug("IMAGE '" + cacheId + "' added to image cache.");
        } else {
            // log.debug("Image " + path + " is allready in font cache.");
        }
    }

    public static ImageData getImage(String cacheId) {
        if (!images.containsKey(cacheId)) {
            loadImage(cacheId);
        } else {
            // log.debug("Found image " + path + " in image cache. Returning.");
        }
        return images.get(cacheId);
    }
}
