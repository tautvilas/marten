package marten.aoe.data.tiles;

import marten.aoe.dto.PointDTO;
import marten.aoe.engine.Map;

public final class Rocks extends StandardTerrainBase {
    public Rocks(Map owner, PointDTO coordinates) {
        super("Rocks", owner, coordinates, 1, null);
    }
}
