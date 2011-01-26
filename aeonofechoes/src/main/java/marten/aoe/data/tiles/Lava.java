package marten.aoe.data.tiles;

import marten.aoe.proposal.dto.DamageDTO;
import marten.aoe.proposal.dto.DamageType;
import marten.aoe.proposal.dto.PointDTO;
import marten.aoe.proposal.engine.Map;

public final class Lava extends StandardTerrainBase {
    public Lava(Map owner, PointDTO coordinates) {
        super("Lava", owner, coordinates, 3, new DamageDTO(10, DamageType.HEAT));
    }
}
