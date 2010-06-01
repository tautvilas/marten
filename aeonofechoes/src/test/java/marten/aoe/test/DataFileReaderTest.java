package marten.aoe.test;

import java.io.IOException;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import marten.aoe.engine.TerrainDatabase;
import marten.aoe.engine.TerrainFeatures;
import marten.aoe.loader.Loader;

/** Tests the proper implementation of Marten's Minimal Data Tree Language and its full set of features by reading some sample definition files
 * @author Petras Ražanskas */
public final class DataFileReaderTest {
    /** Reads "data/terrain_test" file and parses its contents, then tests its correctness 
     * @throws IOException if the an I/O failure occured while reading the test file*/
    @Test public void terrainLoaded () throws IOException {
        Loader.load("data/terrain_test");
        Set<String> definedTerrain = TerrainDatabase.definedTerrain();
        Assert.assertTrue(definedTerrain.contains("grassland"));
        Assert.assertTrue(definedTerrain.contains("forest"));
        Assert.assertTrue(definedTerrain.contains("hill"));
        Assert.assertTrue(definedTerrain.contains("mountain"));
        Assert.assertTrue(definedTerrain.contains("ford"));
        Assert.assertTrue(definedTerrain.contains("water"));
        Assert.assertTrue(definedTerrain.contains("fort"));
        Assert.assertFalse(definedTerrain.contains("lava"));
        Set<TerrainFeatures> sampleFeatures = TerrainDatabase.get("water").features();
        Assert.assertTrue(sampleFeatures.contains(TerrainFeatures.IMPASSABLE));
        Assert.assertTrue(sampleFeatures.contains(TerrainFeatures.WATER));
        Assert.assertFalse(sampleFeatures.contains(TerrainFeatures.FORTIFIED));
    }

}
