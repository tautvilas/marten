package marten.aoe.data.tiles;

import marten.aoe.dto.PointDTO;
import marten.aoe.engine.Map;

public final class Dirt extends StandardTerrainBase {
    public Dirt(Map owner, PointDTO coordinates) {
        super("Dirt", owner, coordinates, 2, null);
    }
}
