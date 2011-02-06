package marten.aoe.engine;

import marten.aoe.dto.PointDTO;

public interface EngineListener {
    void onLocalEvent(LocalEvent event, PointDTO location);
    void onGlobalEvent(GlobalEvent event);
}
