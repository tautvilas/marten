package marten.aoe.test;

import java.io.IOException;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import marten.aoe.obsolete.engine.Engine;
import marten.aoe.obsolete.engine.PathFinder;
import marten.aoe.obsolete.engine.TerrainFeatures;
import marten.aoe.obsolete.engine.Tile;
import marten.aoe.obsolete.engine.TileCoordinate;
import marten.aoe.obsolete.engine.TileDirection;
import marten.aoe.obsolete.engine.TilePath;
import marten.aoe.obsolete.engine.UnitType;
import marten.aoe.obsolete.loader.Loader;

/** Tests the proper implementation of Marten's Minimal Data Tree Language and its full set of features by reading some sample definition files
 * @author Petras Ražanskas */
public final class DataFileReaderTest {
    private Engine engine = new Engine();
    /** Reads "data/terrain_test" file and parses its contents, then tests its correctness 
     * @throws IOException if the an I/O failure occured while reading the test file*/
    @Test public void terrainLoaded () throws IOException {
        Loader.load(this.engine, "data/terrain/TerrainTest");
        Set<String> definedTerrain = this.engine.terrain.definedTerrain();
        Assert.assertTrue(definedTerrain.contains("grassland"));
        Assert.assertTrue(definedTerrain.contains("forest"));
        Assert.assertTrue(definedTerrain.contains("hill"));
        Assert.assertTrue(definedTerrain.contains("mountain"));
        Assert.assertTrue(definedTerrain.contains("ford"));
        Assert.assertTrue(definedTerrain.contains("water"));
        Assert.assertTrue(definedTerrain.contains("fort"));
        Assert.assertFalse(definedTerrain.contains("lava"));
        Set<TerrainFeatures> sampleFeatures = this.engine.terrain.get("water").features();
        Assert.assertTrue(sampleFeatures.contains(TerrainFeatures.UNWALKABLE));        
        Assert.assertFalse(sampleFeatures.contains(TerrainFeatures.FORTIFIED));
    }
    @Test public void mapLoaded () throws IOException {
        // Testing if the whole map is loaded correctly would be rather tedious.
        // Therefore we will test only one of the loaded tiles.
        Loader.load(this.engine, "data/maps/MapTest");
        Assert.assertTrue(this.engine.tileMap.name().equals("test"));
        Tile tile = this.engine.tileMap.get(new TileCoordinate(0, 0));
        Assert.assertTrue(tile.name().equals("The Dwelling of the Mountain God"));
        Set<TerrainFeatures> features = tile.terrain().features();
        Assert.assertTrue(features.contains(TerrainFeatures.VERY_HIGH));
        Assert.assertTrue(features.contains(TerrainFeatures.HARD_TO_WALK));
        Assert.assertFalse(features.contains(TerrainFeatures.HARD_TO_SWIM));
    }
    @Test public void bfwTerrainCorrectness () throws IOException {
        // Not a test per se. Keep a lookout for console messages for unknown options.
        Loader.load(this.engine, "data/terrain/BasicTerrain");
    }
    @Test public void bfw2aoeMapConverterCorrectness () throws IOException {
        // Ditto
        Loader.load(this.engine, "data/maps/Village");
    }
    @Test public void pathfinderCorrectness () throws IOException {
        // First case tries if the shortest possible path is found and
        // if no path is found if movement allowance is insufficient
        // all other things being equal.
        Loader.load(this.engine, "data/maps/PathfinderCase1");
        PathFinder alpha = new PathFinder(this.engine.tileMap, new TileCoordinate(0, 0), UnitType.GROUND, 3);
        TilePath testPath = alpha.findPath(new TileCoordinate(0, 2));
        Assert.assertTrue(testPath.directions().size() == 2);
        for (TileDirection direction : testPath.directions())
            Assert.assertTrue(direction.equals(TileDirection.NORTH));
        PathFinder beta = new PathFinder(this.engine.tileMap, new TileCoordinate(0, 0), UnitType.GROUND, 1);
        Assert.assertNull(beta.findPath(new TileCoordinate(0, 2)));
        // Second case tries if pathfinder loops correctly around
        // impassable terrain features. As a side quest, tests import
        // and tile update features of loader.
        Loader.load(this.engine, "data/maps/PathfinderCase2");
        PathFinder gamma = new PathFinder(this.engine.tileMap, new TileCoordinate(0, 0), UnitType.GROUND, 3);
        TilePath test2Path = gamma.findPath(new TileCoordinate(0, 2));
        Assert.assertNotNull(test2Path);
        Assert.assertTrue(test2Path.directions().size() == 3);
    }
}
