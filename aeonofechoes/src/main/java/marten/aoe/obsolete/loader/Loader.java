package marten.aoe.obsolete.loader;

import java.io.IOException;

import marten.aoe.obsolete.engine.Engine;

/** The tool to load the contents of a Marten's Minimal Data Tree Language file into the game.*/
public final class Loader {
    /** Loads the contents of a given file into the game 
     * @param fileName a <code>String</code> with the name of the file */
    public static void load(Engine engine, String fileName) throws IOException {
        DataTree data = DataFileReader.read(fileName);
        if (data.value().equals("FILE"))
            for (DataTree branch : data.branches())
                if (branch.value().equals("Terrain"))
                    TerrainParser.parse(engine, branch);
                else if (branch.value().equals("Map"))
                    MapParser.parse(engine, branch);
                else if (branch.value().equals("ClearMap"))
                    ClearMapParser.parse(engine, branch);
                else if (branch.value().equals("Import"))
                    ImportParser.parse(engine, branch);
                else if (branch.value().equals("UnitProfile"))
                    UnitProfileParser.parse(engine, branch);
                else if (branch.value().equals("CleanData")) {
                    engine.tileMap.removeAll();
                    engine.terrain.removeAll();
                    engine.unitProfiles.removeAll();
                }
                else
                    System.err.println("Unknown option: "+branch.value());
    }
}
