package marten.aoe.loader;

import java.io.IOException;

import marten.aoe.engine.TerrainDatabase;
import marten.aoe.engine.TileMap;

/** The tool to load the contents of a Marten's Minimal Data Tree Language file into the game.*/
public final class Loader {
    /** Loads the contents of a given file into the game 
     * @param fileName a <code>String</code> with the name of the file */
    public static void load(String fileName) throws IOException {
        DataTree data = DataFileReader.read(fileName);
        if (data.value().equals("FILE"))
            for (DataTree branch : data.branches())
                if (branch.value().equals("Terrain"))
                    TerrainParser.parse(branch);
                else if (branch.value().equals("Map"))
                    MapParser.parse(branch);
                else if (branch.value().equals("ClearMap"))
                    ClearMapParser.parse(branch);
                else if (branch.value().equals("Import"))
                    ImportParser.parse(branch);
                else if (branch.value().equals("CleanData")) {
                    TileMap.removeAll();
                    TerrainDatabase.removeAll();
                }            
    }
}
