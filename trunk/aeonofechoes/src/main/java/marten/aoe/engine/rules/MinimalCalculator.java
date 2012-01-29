package marten.aoe.engine.rules;

import marten.aoe.dto.UnitDTO;

import marten.aoe.dto.TileDTO;

public class MinimalCalculator {
    public int getMovementCost(TileDTO tile, UnitDTO unit) {
        return 1;
    }
}
