package marten.aoe.data.tiles;

import marten.aoe.dto.PointDTO;
import marten.aoe.engine.Map;

public final class Generic extends StandardTerrainBase {
    public Generic(String name, Map owner, PointDTO coordinates) {
        super(name, owner, coordinates, 1, null);
    }
}
