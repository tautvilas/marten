package marten.aoe.engine;

import java.io.IOException;

import marten.aoe.dto.MapMetaDTO;
import marten.aoe.fileio.DataTree;

public final class SimpleMap extends LoadedMap {
    public SimpleMap(Engine engine, MapMetaDTO meta, DataTree mapFile) throws IOException {
        super(engine, meta, mapFile);
    }
    @Override public void onTurnOver() {
    }

}
