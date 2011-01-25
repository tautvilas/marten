package marten.aoe.proposal.tiles;

import marten.aoe.proposal.dto.Point;
import marten.aoe.proposal.engine.Map;

public final class Dirt extends StandardTerrainBase {
    public Dirt(Map owner, Point coordinates) {
        super("Dirt", owner, coordinates, 2, null);
    }
}
