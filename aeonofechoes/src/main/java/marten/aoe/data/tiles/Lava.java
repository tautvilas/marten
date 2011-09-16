package marten.aoe.data.tiles;

import marten.aoe.dto.PointDTO;
import marten.aoe.engine.Map;

public final class Lava extends StandardTerrainBase {
    public Lava(Map owner, PointDTO coordinates) {
        super("Lava", owner, coordinates, 0, null);
    }
}
