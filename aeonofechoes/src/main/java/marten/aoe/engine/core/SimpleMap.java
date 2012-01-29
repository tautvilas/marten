package marten.aoe.engine.core;

import marten.aoe.dto.MapMetaDTO;
import marten.aoe.engine.Engine;
import marten.aoe.fileio.DataTree;

public final class SimpleMap extends LoadedMap {
    public SimpleMap(Engine engine, MapMetaDTO meta, DataTree mapFile) {
        super(engine, meta, mapFile);
    }
    @Override public void onTurnOver() {
    }

}
