package marten.aoe.data.tiles;

import marten.aoe.data.type.TileType;

import org.apache.log4j.Logger;

public class TileLayerFactory {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(TileLayerFactory.class);

    public static TileLayerDTO getTileLayer(String id) {
        TileLayerDTO layer = null;
        if (id.equals(TileLayers.WATER)) {
            layer = new TileLayerDTO(id, 1, TileType.WATER);
            layer.setDefence(0);
        } else {
            layer = new TileLayerDTO(id);
            log.error("Layer " + id + " not defined");
        }
        return layer;
    }
}
