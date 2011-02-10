package marten.aoe.engine;

import marten.aoe.dto.FullTileDTO;
import marten.aoe.dto.PlayerDTO;

public interface EngineListener {
    void onLocalEvent(LocalEvent event, FullTileDTO location);
    void onGlobalEvent(GlobalEvent event);
    PlayerDTO getAssignedPlayer();
}
