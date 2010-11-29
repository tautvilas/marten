package marten.aoe.proposal.engine;

import java.io.IOException;

import marten.aoe.proposal.dto.Point;
import marten.aoe.proposal.loader.DataFileReader;
import marten.aoe.proposal.loader.DataTree;
import marten.aoe.proposal.loader.TileLoader;

/** A branching point for all the maps that have a static layout, which is defined in a DataTree structure in data folder of the game.
 * This, however, does not imply any simplicity in a way the map operates, just that the map is not generated dynamically.*/
public abstract class SimpleMap extends Map {
    /** Creates an instance of a single map from defined file and with given dimensions.
     * These dimension have to agree with the data in the file perfectly for loading to complete successfully.
     * @param fileName the name of the file where the layout is recorded in DataTree format.
     * @param width the width of each of a row in the map.
     * @param height the total number of the rows in the map.
     * @throws IOException whenever there are issues with files or map dimensions do not match the given ones.*/
    public SimpleMap (String fileName, int width, int height) throws IOException {
        super (fileName, width, height);
        DataTree mapData = DataFileReader.read(fileName);
        if (mapData.value().equals("Map")) {
            int y = height - 1;            
            for (DataTree subbranch : mapData.branches()) {
                if (subbranch.value().equals("Row")) {
                    int x = 0;
                    for (DataTree subsubbranch : subbranch.branches()) {
                        this.switchTile(new Point(x, y), TileLoader.loadTile(subsubbranch.value(), this, new Point(x, y)));
                        x++;
                    }
                    if (x != width) {
                        throw new IOException("Row dimension mismatch.");
                    }
                    y--;
                }
            }
            if (y != -1) {
                throw new IOException("Column dimension mismatch.");
            }
        }
    }
}
