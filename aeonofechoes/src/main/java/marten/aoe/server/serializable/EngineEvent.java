package marten.aoe.server.serializable;

import java.io.Serializable;

public enum EngineEvent implements Serializable {
    TILE_UPDATE,
    STREAM_UPDATE,
    TURN_END
}
