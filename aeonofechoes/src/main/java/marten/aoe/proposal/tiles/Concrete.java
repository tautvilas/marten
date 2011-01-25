package marten.aoe.proposal.tiles;

import marten.aoe.proposal.dto.Point;
import marten.aoe.proposal.engine.Map;

public final class Concrete extends StandardTerrainBase {
    public Concrete(Map owner, Point coordinates) {
        super("Concrete", owner, coordinates, 1, null);
    }
}
