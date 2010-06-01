package marten.aoe.loader;

import java.io.IOException;
import java.util.ArrayList;

/** The tool to load the contents of a Marten's Minimal Data Tree Language file into the game.*/
public final class Loader {
    private static ArrayList<Parser> database = new ArrayList<Parser>();
    /** Loads the contents of a given file into the game 
     * @param fileName a <code>String</code> with the name of the file */
    public static void load(String fileName) throws IOException {
        DataTree data = DataFileReader.read(fileName);
        for (Parser parser : database)
            parser.parse(data);
    }
    static {
        database.add(new TerrainParser());
    }
}
