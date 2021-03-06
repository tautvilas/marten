package marten.aoe.engine.rules;

import java.util.List;

import marten.aoe.GameInfo;
import marten.aoe.data.tiles.TileLayers;
import marten.aoe.data.type.DamageType;
import marten.aoe.data.type.UnitType;
import marten.aoe.data.units.Units;
import marten.aoe.dto.MapMetaDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.depreciated.DamageDTO;
import marten.aoe.engine.Action;
import marten.aoe.engine.core.Map;
import marten.aoe.engine.core.Player;
import marten.aoe.engine.core.Tile;
import marten.aoe.engine.core.Unit;

public class AoeRuleset implements Rules {

    @Override
    public void gameStart(Map map, Player[] playerList) {
        MapMetaDTO meta = map.getMeta();
        List<PointDTO> positions = meta.getStartingPositions();
        for (int i = 0; i < playerList.length; i++) {
            Player player = playerList[i];
            player.setEnergyCapacity(100);
            player.setEnergyRate(20);
            player.setEnergy(100);
            PointDTO position = positions.get(i);
            map.spawnUnit(player, Units.BASE, position);
        }
    }

    @Override
    public void performAction(Map map, Player player, PointDTO from,
            PointDTO to, Action action) {
        if (from.equals(to)) {
            return;
        }
        Tile sourceTile = map.getTile(from);
        Unit unit = sourceTile.getUnit();
        if (unit.getUnitType() == UnitType.GROUND) {
            this.performRegimentAction(map, player, from, to, action);
        } else if (unit.getUnitType() == UnitType.BUILDING){
            this.performBuildingAction(map, player, from, to, action);
        }
    }

    private void performBuildingAction(Map map, Player player, PointDTO from,
            PointDTO to, Action action) {
        Unit unit = map.getTile(from).getUnit();
        Tile targetTile = map.getTile(to);
        Tile sourceTile = map.getTile(from);
        if (unit.getName().equals(Units.BASE)
                && targetTile.distanceTo(sourceTile) == 1
                && !targetTile.isOccupied()) {
            switch (action) {
            case FIRST:
                if (player.getMoney() < 20) break;
                player.setEnergy(player.getMoney() - 20);
                map.spawnUnit(player, Units.Worker, to);
                break;
        }
        }
    }

    private void performRegimentAction(Map map, Player player, PointDTO from,
            PointDTO to, Action action) {
        Tile targetTile = map.getTile(to);
        Tile sourceTile = map.getTile(from);
        switch (action) {
            case FIRST:
                map.moveUnit(player, from, to);
                break;
            case SECOND:
                Unit unit = sourceTile.getUnit();
                if (!targetTile.isOccupied()) break;
                Unit unit2 = targetTile.getUnit();
                int d1 = GameInfo.calculator.getDefence(sourceTile.getDTO(player), unit.getDetails());
                int d2 = GameInfo.calculator.getDefence(targetTile.getDTO(player), unit2.getDetails());
                int a1 = GameInfo.calculator.getAttack(sourceTile.getDTO(player), unit.getDetails());
                int a2 = GameInfo.calculator.getAttack(targetTile.getDTO(player), unit2.getDetails());
                int result = GameInfo.calculator.attack(d1, d2, a1, a2);
                unit2.applyDamage(new DamageDTO(result, DamageType.MECHANICAL));
                break;
        }
    }

    @Override
    public void turnEnd(Map map, Player[] playerList) {
        for (Player player : playerList) {
            int energyRate = 0;
            int energyCapacity = 0;
            List<Unit> units = map.getAllUnits(player);
            for (Unit unit : units) {
                Tile location = unit.getLocation();
                if (!location.isPowered(player)) {
                    unit.applyDamage(new DamageDTO(2, DamageType.PSYCHOLOGICAL));
                }
                if (location.hasLayer(TileLayers.CITY)) {
                    energyRate += 100;
                }
                if (unit.getDetails().getUnitType() == UnitType.BUILDING) {
                    energyRate += 50;
                    energyCapacity += 100;
                } else {
                    energyRate -= 20;
                }
            }
            int energy = energyRate + player.getMoney();
            if (energyCapacity < energy) {
                energy = energyCapacity;
            }
            if (energy < 0) {
                energy = 0;
            }
            player.setEnergyRate(energyRate);
            player.setEnergyCapacity(energyCapacity);
            player.setEnergy(energy);
        }
    }

    @Override
    public boolean checkVictory(Map map, Player player) {
        return false;
    }
}
