package marten.aoe.engine.rules;

import marten.aoe.data.tiles.TileLayerDTO;
import marten.aoe.data.type.TileType;
import marten.aoe.data.type.UnitType;
import marten.aoe.data.units.UnitDetails;
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

    public int getDefence(TileDTO tile, UnitDetails unit) {
        TileLayerDTO base = tile.getLayers().get(0);
        return base.getDefence() + unit.getDefence();
    }

    public int getAttack(TileDTO tile, UnitDetails unit) {
        return unit.getMeleeDamage().getMaxDamage();
    }

    public int attack(int d1, int d2, int a1, int a2) {
        System.out.println("Attack:" + a1 + "Defence" + d2);
        return a1 - d2;
    }
}
