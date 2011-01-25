package marten.aoe.proposal.tiles;

import marten.aoe.proposal.dto.DamageDTO;
import marten.aoe.proposal.dto.DamageType;
import marten.aoe.proposal.dto.Point;
import marten.aoe.proposal.engine.Map;

public final class Lava extends StandardTerrainBase {
    public Lava(Map owner, Point coordinates) {
        super("Lava", owner, coordinates, 3, new DamageDTO(10, DamageType.HEAT));
    }
}
