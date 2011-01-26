package marten.aoe.proposal.tiles;

import marten.aoe.proposal.dto.PointDTO;
import marten.aoe.proposal.engine.Map;

public final class Rocks extends StandardTerrainBase {
    public Rocks(Map owner, PointDTO coordinates) {
        super("Rocks", owner, coordinates, 1, null);
    }
}
