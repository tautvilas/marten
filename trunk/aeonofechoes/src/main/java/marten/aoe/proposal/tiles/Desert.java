package marten.aoe.proposal.tiles;

import marten.aoe.proposal.dto.Point;
import marten.aoe.proposal.engine.Map;

public final class Desert extends StandardTerrainBase {
    public Desert(Map owner, Point coordinates) {
        super("Desert", owner, coordinates, 2, null);
    }
}
