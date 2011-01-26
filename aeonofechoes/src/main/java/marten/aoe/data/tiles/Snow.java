package marten.aoe.data.tiles;

import marten.aoe.proposal.dto.PointDTO;
import marten.aoe.proposal.engine.Map;

public final class Snow extends StandardTerrainBase {
    public Snow(Map owner, PointDTO coordinates) {
        super("Snow", owner, coordinates, 2, null);
    }
}
