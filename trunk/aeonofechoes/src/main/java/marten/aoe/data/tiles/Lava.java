package marten.aoe.data.tiles;

import marten.aoe.dto.DamageDTO;
import marten.aoe.dto.DamageType;
import marten.aoe.dto.PointDTO;
import marten.aoe.engine.Map;

public final class Lava extends StandardTerrainBase {
    public Lava(Map owner, PointDTO coordinates) {
        super("Lava", owner, coordinates, 3, new DamageDTO(10, DamageType.HEAT));
    }
}
