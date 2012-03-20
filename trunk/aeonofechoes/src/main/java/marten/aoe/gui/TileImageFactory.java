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
    @SuppressWarnings("unused")
    private static org.apache.log4j.Logger log = Logger
            .getLogger(TileImageFactory.class);

    private static HashMap<String, LayerPriorities> priorities = new HashMap<String, LayerPriorities>();

    public static void addLayer(String[] priorities, String name,
            String imagePath) {
        TileImageFactory.priorities.put(name, new LayerPriorities(name,
                priorities));
        ImageCache.loadImage(imagePath, name);
    }

    public static String getTileGuiId(TileDTO tile, TileDTO[] surrounds) {
        String tid = TileImageFactory.getLayersId(tile.getLayerIds());
        for (int i = 0; i < surrounds.length; i++) {
            String surround = "null";
            if (surrounds[i] != null) {
                surround = surrounds[i].getLayerIds()[0];
            }
            tid += "_" + surround;
        }
        if (!tile.getVisibility()) {
            tid += "_fog";
        }
        if (!tile.isPowered()) {
            tid += "_pow";
        }
        return tid;
    }

    private static String getLayersId(String[] layers) {
        layers = TileImageFactory.sortLayers(layers);
        String name = "";
        for (int i = 0; i < layers.length; i++) {
            name += layers[i];
            if (i != layers.length - 1) {
                name += "_";
            }
        }
        return name;
    }

    public static String[] sortLayers(String[] layers) {
        ArrayList<LayerPriorities> collection = new ArrayList<LayerPriorities>();
        for (String layer : layers) {
            LayerPriorities overlay = TileImageFactory.priorities.get(layer);
            if (overlay == null) {
                throw new RuntimeException("Layer not in cache: " + layer);
            } else {
                collection.add(overlay);
            }
        }
        Collections.sort(collection);
        String[] result = new String[collection.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = collection.get(i).getType();
        }
        return result;
    }

    public static ImageData getTile(TileDTO tileDto, TileDTO[] surrounds) {
        String name = TileImageFactory.getTileGuiId(tileDto, surrounds);
        String[] layers = tileDto.getLayerIds();
        layers = TileImageFactory.sortLayers(layers);
        ImageData tile = ImageCache.getImage(layers[0]);
        // blend base layers with masks
        Integer start = null;
        int slen = surrounds.length;
        String[] bases = new String[slen];
        for (int i = 0; i < slen + 1; i++) {
            if (surrounds[i % slen] == null) {
                bases[i % slen] = null;
                continue;
            }
            bases[i % slen] = TileImageFactory.sortLayers(surrounds[i % slen].getLayerIds())[0];
            if ((i != 0 && (bases[i -1] == null || !bases[i - 1].equals(bases[i % slen])))) {
                start = i % surrounds.length;
            }
        }
        if (start != null) {
            int blen = 1;
            String pb = bases[start];
            int pi = start;
            for (int i = start + 1; i <= surrounds.length + start; i++) {
                String s = bases[i % slen];
                if (s != null && s.equals(pb)) {
                    blen++;
                } else {
                    if (pb != null) {
                        LayerPriorities p1 = TileImageFactory.priorities.get(layers[0]);
                        LayerPriorities p2 = TileImageFactory.priorities.get(pb);
                        if (p1.compareTo(p2) < 0) {
                            ImageData surround = ImageCache.getImage(pb);
                            ImageData image = ImageCache.getImage("msk-" + blen + "-" + pi);
                            tile = ImageTransformations.blend(tile, surround, image);
                        }
                    }
                    blen = 1;
                }
                pb = s;
                pi = i % slen;
            }
        } else {
            if (bases[0] != null) {
                LayerPriorities p1 = TileImageFactory.priorities.get(layers[0]);
                LayerPriorities p2 = TileImageFactory.priorities.get(bases[0]);
                if (p1.compareTo(p2) < 0) {
                    ImageData surround = ImageCache.getImage(bases[0]);
                    ImageData image = ImageCache.getImage("msk-6");
                    tile = ImageTransformations.blend(tile, surround, image);
                }
            }
        }
        // append other layers
        for (int i = 1; i < layers.length; i++) {
            tile = ImageTransformations.blend(tile,
                    ImageCache.getImage(layers[i]));
        }
        ImageCache.addImage(name, tile);
        return tile;
    }

    /** Methods used by map editor **/

    @SuppressWarnings("deprecation")
    public static TileDTO blendTile(TileDTO base, String layer) {
        List<String> layers = Arrays.asList(base.getLayerIds());
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
        return new TileDTO(TileImageFactory.sortLayers(array),
                base.getCoordinates(), base.getUnit());
    }

    public static ArrayList<LayerPriorities> getSortedLayerTypes() {
        ArrayList<LayerPriorities> result = new ArrayList<LayerPriorities>();
        for (String key : TileImageFactory.priorities.keySet()) {
            result.add(TileImageFactory.priorities.get(key));
        }
        Collections.sort(result);
        return result;
    }
}
