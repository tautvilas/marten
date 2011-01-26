package marten.aoe.data.tiles;

import marten.aoe.dto.PointDTO;
import marten.aoe.engine.Map;

public final class Snow extends StandardTerrainBase {
    public Snow(Map owner, PointDTO coordinates) {
        super("Snow", owner, coordinates, 2, null);
    }
}
