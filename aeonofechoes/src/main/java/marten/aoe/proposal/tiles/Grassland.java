package marten.aoe.proposal.tiles;

import marten.aoe.proposal.dto.Point;
import marten.aoe.proposal.engine.Map;

public final class Grassland extends StandardTerrainBase {
    public Grassland(Map owner, Point coordinates) {
        super("Concrete", owner, coordinates, 1, null);
    }
}
