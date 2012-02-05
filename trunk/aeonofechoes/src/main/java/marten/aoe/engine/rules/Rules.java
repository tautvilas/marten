package marten.aoe.engine.rules;

import marten.aoe.dto.PointDTO;
import marten.aoe.engine.Action;
import marten.aoe.engine.core.Map;
import marten.aoe.engine.core.Player;

public interface Rules {
    public void gameStart(Map map, Player[] playerList);

    public void performAction(Map map, Player player, PointDTO from, PointDTO to, Action action);

    public void turnEnd(Map map, Player[] playerList);

    public boolean checkVictory(Map map, Player player);
}
