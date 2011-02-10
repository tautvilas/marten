package marten.aoe.engine;

import marten.aoe.dto.TileDTO;

public interface EngineListener {
    void onLocalEvent(LocalEvent event, TileDTO tileDTO);
    void onGlobalEvent(GlobalEvent event);
}
