package marten.aoe.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import marten.aoe.Path;
import marten.aoe.engine.loader.MapLoader;
import marten.aoe.fileio.DataFileHandler;
import marten.aoe.fileio.DataTree;

public final class MapDTO implements Serializable {
    private static final long serialVersionUID = 2061015657078949829L;
    private final TileDTO[][] tileMap;
    private final MapMetaDTO meta;
    public MapDTO (TileDTO[][] tileMap, MapMetaDTO meta) {
        this.tileMap = tileMap;
        this.meta = meta;
    }
    public TileDTO getTileDTO (PointDTO location) {
        return this.tileMap[location.getX()][location.getY()];
    }
    public MapMetaDTO getMeta() {
        return this.meta;
    }
    public TileDTO[][] getTileMap() {
        return this.tileMap;
    }
    
    public static final MapDTO loadFromMapFile (String mapName) {
        List<String> availableMaps = MapLoader.getAvailableSimpleMaps();
        if (!availableMaps.contains(mapName)) {
            throw new RuntimeException("Map " + mapName + " not found.");
        }
        DataTree mapFile;
        try {
            mapFile = DataFileHandler.read(Path.MAP_DATA_PATH + mapName + ".map");             
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to instantiate " + mapName + ".");
        }
        MapMetaDTO meta = MapLoader.retrieveMapMetaData(mapFile);
        TileDTO[][] map = new TileDTO[meta.getHeight()][meta.getWidth()];
        if (mapFile.value().equals("FILE")) {
            DataTree mapData = mapFile.branches().get(1);
            if (mapData.value().equals("Map")) {
                int y = meta.getHeight() - 1;
                for (DataTree subbranch : mapData.branches()) {
                    if (subbranch.value().equals("Row")) {
                        int x = 0;
                        for (DataTree subsubbranch : subbranch.branches()) {
                            if (subsubbranch.value().equals("Tile")) {
                                List<String> layers = new ArrayList<String>();
                                for (DataTree subsubsubbranch : subsubbranch.branches()) {
                                    layers.add(subsubsubbranch.value());                                    
                                }
                                map[x][y] = new TileDTO(layers.toArray(new String[] {}), new PointDTO(x,y), null);
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
        return new MapDTO(map, meta);
    }
    
}
