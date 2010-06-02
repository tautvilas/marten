package marten.aoe.loader;

import java.io.IOException;

final class ImportParser {
    public static void parse(DataTree branch) throws IOException {
        if (branch.value().equals("Import"))
            for (DataTree subbranch : branch.branches())
                Loader.load(subbranch.value());
    }
}
