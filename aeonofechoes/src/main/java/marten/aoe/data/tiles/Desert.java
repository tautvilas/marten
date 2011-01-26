package marten.aoe.data.tiles;

import marten.aoe.dto.PointDTO;
import marten.aoe.engine.Map;

public final class Desert extends StandardTerrainBase {
    public Desert(Map owner, PointDTO coordinates) {
        super("Desert", owner, coordinates, 2, null);
    }
}
