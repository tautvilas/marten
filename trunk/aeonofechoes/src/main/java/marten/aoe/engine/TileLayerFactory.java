package marten.aoe.engine;

import marten.aoe.dto.TileLayerDTO;

public class TileLayerFactory {
    public static TileLayerDTO getTileLayer(String id) {
        return new TileLayerDTO(id, 1);
    }
}
