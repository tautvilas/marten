package marten.aoe.test;

import java.io.IOException;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import marten.aoe.engine.TerrainDatabase;
import marten.aoe.engine.TerrainFeatures;
import marten.aoe.engine.Tile;
import marten.aoe.engine.TileCoordinate;
import marten.aoe.engine.TileMap;
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
    @Test public void mapLoaded () throws IOException {
        // Testing if the whole map is loaded correctly would be rather tedious.
        // Therefore we will test only one of the loaded tiles.
        Loader.load("data/map_test");
        Assert.assertTrue(TileMap.name().equals("test"));
        Tile tile = TileMap.get(new TileCoordinate(0, 0));
        Assert.assertTrue(tile.name().equals("The Dwelling of the Mountain God"));
        Set<TerrainFeatures> features = tile.terrain().features();
        Assert.assertTrue(features.contains(TerrainFeatures.MOUNTAIN));
        Assert.assertTrue(features.contains(TerrainFeatures.DIFFICULT));
        Assert.assertFalse(features.contains(TerrainFeatures.WATER));
    }
}
