package marten.aoe.proposal.tiles;

import marten.aoe.proposal.dto.Point;
import marten.aoe.proposal.engine.Map;

public final class Snow extends StandardTerrainBase {
    public Snow(Map owner, Point coordinates) {
        super("Snow", owner, coordinates, 2, null);
    }
}
