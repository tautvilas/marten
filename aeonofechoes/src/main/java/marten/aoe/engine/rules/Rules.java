package marten.aoe.engine.rules;

import marten.aoe.dto.PlayerDTO;
import marten.aoe.engine.Map;

public interface Rules {
    public void gameStart(Map map, PlayerDTO[] playerList);
}
