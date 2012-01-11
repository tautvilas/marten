package marten.aoe.rules;

import marten.aoe.dto.PlayerDTO;
import marten.aoe.engine.Engine;
import marten.aoe.engine.Map;

public interface Rules {
    public void gameStart(Engine engine, Map map, PlayerDTO[] playerList);
}
