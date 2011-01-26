package marten.aoe.data.tiles;

import marten.aoe.proposal.dto.PointDTO;
import marten.aoe.proposal.engine.Map;

public final class Dirt extends StandardTerrainBase {
    public Dirt(Map owner, PointDTO coordinates) {
        super("Dirt", owner, coordinates, 2, null);
    }
}
