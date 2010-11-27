package marten.aoe.depreciated.loader;

import java.io.IOException;

import marten.aoe.depreciated.engine.Engine;

final class ImportParser {
    public static void parse(Engine engine, DataTree branch) throws IOException {
        if (branch.value().equals("Import"))
            for (DataTree subbranch : branch.branches())
                Loader.load(engine, subbranch.value());
    }
}
