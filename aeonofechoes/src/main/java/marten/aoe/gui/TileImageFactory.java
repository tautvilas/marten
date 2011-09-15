package marten.aoe.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import marten.age.graphics.image.ImageCache;
import marten.age.graphics.image.ImageData;
import marten.age.graphics.image.ImageTransformations;
import marten.aoe.dto.TileDTO;

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
        return TileImageFactory.getTile(new String[] { layer });
    }

    public static String getTileGuiId(TileDTO tile) {
        return TileImageFactory.getTileGuiId(tile.getLayers());
    }

    public static String getTileGuiId(String[] layers) {
        layers = TileImageFactory.sortLayers(layers);
        String name = "";
        for (int i = 0; i < layers.length; i++) {
            name += layers[i].toLowerCase();
            if (i != layers.length - 1) {
                name += "_";
            }
        }
        return name;
    }

    public static String[] sortLayers(String[] layers) {
        ArrayList<TileLayer> collection = new ArrayList<TileLayer>();
        for (String layer : layers) {
            TileLayer overlay = TileImageFactory.priorities.get(layer
                    .toLowerCase());
            if (overlay == null) {
                throw new RuntimeException("Layer not in cache: "
                        + layer.toLowerCase());
            } else {
                collection.add(TileImageFactory.priorities.get(layer
                        .toLowerCase()));
            }
        }
        Collections.sort(collection);
        String[] result = new String[collection.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = collection.get(i).getType();
        }
        return result;
    }

    public static ImageData getTile(String[] layers) {
        String name = TileImageFactory.getTileGuiId(layers);
        if (TileImageFactory.images.containsKey(name)) {
            return TileImageFactory.images.get(name);
        } else {
            layers = TileImageFactory.sortLayers(layers);
            ImageData tile = TileImageFactory.images.get(layers[0]);
            for (int i = 1; i < layers.length; i++) {
                tile = ImageTransformations.blend(tile, TileImageFactory.images
                        .get(layers[i]));
            }
            log.info("Putting " + name + " tile image to tile image cache");
            TileImageFactory.images.put(name, tile);
            return tile;
        }
    }

    /** Methods used by map editor **/

    public static TileDTO blendTile(TileDTO base, String layer) {
        List<String> layers = Arrays.asList(base.getLayers());
        List<String> mergedLayers = new ArrayList<String>();
        for (String baseLayer : layers) {
            String p1 = TileImageFactory.priorities.get(baseLayer)
                    .getPriorities()[0];
            String p2 = TileImageFactory.priorities.get(layer).getPriorities()[0];
            if (p1.equals(p2)) {
                continue;
            } else {
                mergedLayers.add(baseLayer);
            }
        }
        mergedLayers.add(layer);
        String[] array = new String[mergedLayers.size()];
        mergedLayers.toArray(array);
        return new TileDTO(array, base.getCoordinates(), base.getUnit());
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
