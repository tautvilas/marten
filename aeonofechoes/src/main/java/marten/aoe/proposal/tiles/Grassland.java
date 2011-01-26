package marten.aoe.proposal.tiles;

import marten.aoe.proposal.dto.PointDTO;
import marten.aoe.proposal.engine.Map;

public final class Grassland extends StandardTerrainBase {
    public Grassland(Map owner, PointDTO coordinates) {
        super("Concrete", owner, coordinates, 1, null);
    }
}
