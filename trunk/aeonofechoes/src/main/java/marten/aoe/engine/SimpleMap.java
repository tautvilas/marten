package marten.aoe.engine;

import java.io.IOException;

import marten.aoe.Path;
import marten.aoe.dto.PointDTO;
import marten.aoe.engine.loader.DataFileReader;
import marten.aoe.engine.loader.DataTree;
import marten.aoe.engine.loader.TileLoader;

/** A branching point for all the maps that have a static layout, which is defined in a DataTree structure in data folder of the game.
 * This, however, does not imply any simplicity in a way the map operates, just that the map is not generated dynamically.*/
public abstract class SimpleMap extends Map {
    /** Creates an instance of a single map from defined file and with given dimensions.
     * These dimension have to agree with the data in the file perfectly for loading to complete successfully.
     * @param fileName the name of the file where the layout is recorded in DataTree format.
     * @param width the width of each of a row in the map.
     * @param height the total number of the rows in the map.
     * @throws IOException whenever there are issues with files or map dimensions do not match the given ones.*/
    public SimpleMap (Engine engine, String fileName, int width, int height) throws IOException {
        super (engine, fileName, width, height);
        DataTree mapFile = DataFileReader.read(Path.MAP_DATA_PATH + fileName);
        if (mapFile.value().equals("FILE")) {
            DataTree mapData = mapFile.branches().get(0);
            if (mapData.value().equals("Map")) {
                int y = height - 1;
                for (DataTree subbranch : mapData.branches()) {
                    if (subbranch.value().equals("Row")) {
                        int x = 0;
                        for (DataTree subsubbranch : subbranch.branches()) {
                            if (subsubbranch.value().equals("Tile")) {
                                Tile tile = null;
                                for (DataTree subsubsubbranch : subsubbranch.branches()) {
                                    if (tile == null) {
                                        tile = TileLoader.loadTile(subsubsubbranch.value(), this, new PointDTO(x, y));
                                    }
                                    else {
                                        tile = TileLoader.loadLayer(subsubsubbranch.value(), tile);
                                    }
                                }
                                this.switchTile(tile);
                                x++;
                            }
                        }
                        if (x != width) {
                            throw new IOException("Row dimension mismatch. Expected: "+width+", actual: "+x);
                        }
                        y--;
                    }
                }
                if (y != -1) {
                    throw new IOException("Column dimension mismatch. Expected: "+height+", actual: "+(height - 1 - y));
                }
            }
            else {
                throw new IOException("Not a map file");
            }
        }
        else {
            throw new IOException("Unknown major error");
        }
    }
}
