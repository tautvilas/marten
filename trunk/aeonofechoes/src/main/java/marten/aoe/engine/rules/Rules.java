package marten.aoe.engine.rules;

import marten.aoe.dto.PointDTO;
import marten.aoe.engine.Action;
import marten.aoe.engine.core.Map;
import marten.aoe.engine.core.PlayerDTO;

public interface Rules {
    public void gameStart(Map map, PlayerDTO[] playerList);

    public void performAction(Map map, PlayerDTO player, PointDTO from, PointDTO to, Action action);
}
