package marten.aoe.proposal.tiles;

import marten.aoe.proposal.dto.Point;
import marten.aoe.proposal.engine.Map;

public final class Rocks extends StandardTerrainBase {
    public Rocks(Map owner, Point coordinates) {
        super("Rocks", owner, coordinates, 1, null);
    }
}
