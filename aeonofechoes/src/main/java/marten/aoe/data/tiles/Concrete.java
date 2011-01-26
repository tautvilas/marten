package marten.aoe.data.tiles;

import marten.aoe.dto.PointDTO;
import marten.aoe.engine.Map;

public final class Concrete extends StandardTerrainBase {
    public Concrete(Map owner, PointDTO coordinates) {
        super("Concrete", owner, coordinates, 1, null);
    }
}
