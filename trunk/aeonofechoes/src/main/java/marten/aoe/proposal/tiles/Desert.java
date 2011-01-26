package marten.aoe.proposal.tiles;

import marten.aoe.proposal.dto.PointDTO;
import marten.aoe.proposal.engine.Map;

public final class Desert extends StandardTerrainBase {
    public Desert(Map owner, PointDTO coordinates) {
        super("Desert", owner, coordinates, 2, null);
    }
}
