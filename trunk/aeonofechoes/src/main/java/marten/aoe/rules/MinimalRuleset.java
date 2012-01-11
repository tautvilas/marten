package marten.aoe.rules;

import java.util.List;

import marten.aoe.dto.MapMetaDTO;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.engine.Engine;
import marten.aoe.engine.Map;

public class MinimalRuleset implements Rules {

    public void gameStart(Engine engine, Map map, PlayerDTO[] playerList) {
        MapMetaDTO meta = map.getMeta();
        List<PointDTO> positions = meta.getStartingPositions();
        for (int i = 0; i < playerList.length; i++) {
            PointDTO position = positions.get(i);
            engine.spawnUnit(playerList[i], "Dwarf", position);
        }
    }

}
