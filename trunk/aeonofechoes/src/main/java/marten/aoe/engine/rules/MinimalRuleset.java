package marten.aoe.engine.rules;

import java.util.List;

import marten.aoe.dto.MapMetaDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.engine.Action;
import marten.aoe.engine.core.Map;
import marten.aoe.engine.core.Player;
import marten.aoe.engine.core.Regiment;
import marten.aoe.engine.core.Tile;

public class MinimalRuleset implements Rules {

    public void gameStart(Map map, Player[] playerList) {
        MapMetaDTO meta = map.getMeta();
        List<PointDTO> positions = meta.getStartingPositions();
        for (int i = 0; i < playerList.length; i++) {
            PointDTO position = positions.get(i);
            map.spawnUnit(playerList[i], "Dwarf", position);
        }
    }

    @Override
    public void performAction(Map map, Player player, PointDTO from,
            PointDTO to, Action action) {
        Tile targetTile = map.getTile(to);
        Tile sourceTile = map.getTile(from);
        Regiment unit = (Regiment)sourceTile.getUnit();
        switch (action) {
            case FIRST:
                map.moveUnit(player, from, to);
                break;
            case SECOND:
                if (targetTile.distanceTo(map.getTile(from)) != 1) {
                    return;
                }
                targetTile.applyDamage(unit.getMeleeDamage());
                unit.applyMovementCost(unit.getMovementAllowance());
                break;
            case THIRD:
                if (unit.getAttackRange() < 2) {
                    return;
                }
                if (targetTile.distanceTo(sourceTile) < 2 && targetTile.distanceTo(sourceTile) > unit.getAttackRange()) {
                    return;
                }
                targetTile.applyDamage(unit.getRangedDamage());
                unit.applyMovementCost(unit.getMovementAllowance());
                break;
        }
    }

}
