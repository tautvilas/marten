package marten.aoe.engine;

import marten.aoe.data.tiles.TileLayers;
import marten.aoe.dto.TileLayerDTO;
import marten.aoe.dto.TileType;

import org.apache.log4j.Logger;

public class TileLayerFactory {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(TileLayerFactory.class);

    public static TileLayerDTO getTileLayer(String id) {
        TileLayerDTO layer = null;
        if (id.equals(TileLayers.WATER)) {
            layer = new TileLayerDTO(id, 1, TileType.WATER);
        } else {
            layer = new TileLayerDTO(id);
            log.error("Layer " + id + " not defined");
        }
        return layer;
    }
}
