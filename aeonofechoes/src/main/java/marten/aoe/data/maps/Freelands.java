package marten.aoe.data.maps;

import java.io.IOException;

import marten.aoe.engine.Engine;
import marten.aoe.engine.LoadedMap;
import marten.aoe.engine.loader.MapLoader;
import marten.aoe.fileio.DataFileHandler;

@Deprecated public final class Freelands extends LoadedMap {

    public Freelands(Engine engine) throws IOException {
        // FIXME: this is rather crude mock up to leave this class working despite the changes
        super (engine, MapLoader.retrieveMapMetaData("2pFreelands"), DataFileHandler.read("2pFreelands.map"));
    }

    @Override public void onTurnOver() {
    }

}
