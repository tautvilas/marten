package marten.aoe.engine.rules;

import marten.aoe.data.type.TileType;
import marten.aoe.data.type.UnitType;
import marten.aoe.dto.TileLayerDTO;
import marten.aoe.dto.UnitDTO;

import marten.aoe.dto.TileDTO;

public class MinimalCalculator {
    public int getMovementCost(TileDTO tile, UnitDTO unit) {
        TileLayerDTO base = tile.getLayers().get(0);
        if (base.getType() == TileType.WATER && unit.getType() == UnitType.GROUND) {
            return 4;
        } else {
            return base.getGroundMovementCost();
        }
    }
}
