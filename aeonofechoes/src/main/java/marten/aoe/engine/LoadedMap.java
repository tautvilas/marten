package marten.aoe.engine;

import java.io.IOException;

import marten.aoe.dto.MapMetaDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileLayerDTO;
import marten.aoe.fileio.DataTree;

import org.apache.log4j.Logger;

/** A branching point for all the maps that have a static layout, which is defined in a DataTree structure in data folder of the game.
 * This, however, does not imply any simplicity in a way the map operates, just that the map is not generated dynamically.*/
public abstract class LoadedMap extends Map {
    @SuppressWarnings("unused")
    private static org.apache.log4j.Logger log = Logger
            .getLogger(LoadedMap.class);
    /** Creates an instance of a single map from defined file and with given dimensions.
     * These dimension have to agree with the data in the file perfectly for loading to complete successfully.
     * @param fileName the name of the file where the layout is recorded in DataTree format.
     * @param width the width of each of a row in the map.
     * @param height the total number of the rows in the map.
     * @throws IOException whenever there are issues with files or map dimensions do not match the given ones.*/
    public LoadedMap (Engine engine, MapMetaDTO meta, DataTree mapFile) {
        super (engine, meta);
        if (mapFile.value().equals("FILE")) {
            // mapFile.branches().get(0) would get the map header, which is not interesting any more at this point
            DataTree mapData = mapFile.branches().get(1);
            if (mapData.value().equals("Map")) {
                int y = meta.getHeight() - 1;
                for (DataTree subbranch : mapData.branches()) {
                    if (subbranch.value().equals("Row")) {
                        int x = 0;
                        for (DataTree subsubbranch : subbranch.branches()) {
                            if (subsubbranch.value().equals("Tile")) {
                                Tile tile = new Tile(this, new PointDTO(x, y));
                                for (DataTree subsubsubbranch : subsubbranch.branches()) {
                                    TileLayerDTO layer = new TileLayerDTO(subsubsubbranch.value(), 1);
                                    tile.addLayer(layer);
                                }
                                this.switchTile(tile);
                                x++;
                            }
                        }
                        if (x != meta.getWidth()) {
                            throw new IllegalArgumentException("Row dimension mismatch. Expected: "+meta.getWidth()+", actual: "+x);
                        }
                        y--;
                    }
                }
                if (y != -1) {
                    throw new IllegalArgumentException("Column dimension mismatch. Expected: "+meta.getHeight()+", actual: "+(meta.getHeight() - 1 - y));
                }
            }
            else {
                throw new IllegalArgumentException("Not a map file");
            }
        }
        else {
            throw new IllegalArgumentException("Unknown major error");
        }
    }
}
