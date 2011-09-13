package marten.aoe.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import marten.age.graphics.image.ImageCache;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.image.ImageTransformations;

import org.apache.log4j.Logger;

public class TileImageFactory {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(TileImageFactory.class);

    private static HashMap<String, TileLayer> priorities = new HashMap<String, TileLayer>();
    private static HashMap<String, ImageData> images = new HashMap<String, ImageData>();

    public static void addLayer(String[] priorities, String name,
            String imagePath) {
        if (TileImageFactory.priorities.containsKey(name)) {
            log.error("Layer '" + name + "' was allready added");
            return;
        }
        TileImageFactory.priorities.put(name, new TileLayer(name, priorities));
        TileImageFactory.images.put(name, ImageCache.getImage(imagePath));
    }

    public static ImageData getTile(String layer) {
        layer = layer.toLowerCase();
        if (!images.containsKey(layer)) {
            log.error("Could not find image for layer '" + layer + "'");
        }
        return TileImageFactory.images.get(layer);
    }

    public static ImageData getTile(String[] layers) {
        ArrayList<TileLayer> collection = new ArrayList<TileLayer>();
        for (String layer : layers) {
            collection.add(TileImageFactory.priorities.get(layer));
        }
        Collections.sort(collection);
        ImageData tile = TileImageFactory.images.get(collection.get(0)
                .getType());
        for (int i = 1; i < collection.size(); i++) {
            tile = ImageTransformations.blend(tile, TileImageFactory.images
                    .get(collection.get(i).getType()));
        }
        return tile;
    }

    public static ArrayList<TileLayer> getSortedLayerTypes() {
        ArrayList<TileLayer> result = new ArrayList<TileLayer>();
        for (String key : TileImageFactory.priorities.keySet()) {
            result.add(TileImageFactory.priorities.get(key));
        }
        Collections.sort(result);
        return result;
    }
}
