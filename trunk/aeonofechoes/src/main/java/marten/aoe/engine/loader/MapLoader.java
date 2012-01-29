package marten.aoe.engine.loader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import marten.aoe.Path;
import marten.aoe.dto.MapMetaDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.engine.Engine;
import marten.aoe.engine.core.Map;
import marten.aoe.engine.core.SimpleMap;
import marten.aoe.fileio.DataFileHandler;
import marten.aoe.fileio.DataTree;

public final class MapLoader {
    public static final List<String> getAvailableMaps () {
        return new ArrayList<String>();
    }
    public static final List<String> getAvailableSimpleMaps () {
        File mapDirectory = new File(Path.MAP_DATA_PATH);
        String[] mapList = mapDirectory.list();
        List<String> mapNameList = new ArrayList<String>();
        for (String mapName : mapList) {
            if (mapName.matches(".*\\.map")) {
                mapNameList.add(mapName.split("\\.")[0]);
            }
        }
        return mapNameList;
    }
    public static MapMetaDTO retrieveMapMetaData (String mapName) throws IOException {
        DataTree mapFile = DataFileHandler.read(Path.MAP_DATA_PATH + mapName + ".map");
        return MapLoader.retrieveMapMetaData(mapFile);
    }        
    public static MapMetaDTO retrieveMapMetaData (DataTree mapFile) {
        if (mapFile.value().equals("FILE")) {
            DataTree mapHeader = mapFile.branches().get(0);
            int width = 0;
            int height = 0;
            String name = "";
            List<PointDTO> startingPositions = new ArrayList<PointDTO>();
            for (DataTree entry : mapHeader.branches()) {
                if (entry.value().equals("KEYVALUE")) {
                    String key = entry.branches().get(0).value();
                    String value = entry.branches().get(1).value();
                    if (key.equals("Width")) {
                        width = Integer.parseInt(value);                        
                    }
                    if (key.equals("Height")) {
                        height = Integer.parseInt(value);                        
                    }
                    if (key.equals("Name")) {
                        name = value;
                    }
                } else if (entry.value().equals("Player")) {
                    int x = 0;
                    int y = 0;
                    for (DataTree playerEntry : entry.branches()) {
                        if (playerEntry.value().equals("KEYVALUE")) {
                            String key = playerEntry.branches().get(0).value();
                            String value = playerEntry.branches().get(1).value();
                            if (key.equals("StartX")) {
                                x = Integer.parseInt(value);
                            }
                            if (key.equals("StartY")) {
                                y = Integer.parseInt(value);
                            }
                        }
                    }
                    startingPositions.add(new PointDTO(x, y));
                }                
            }
            return new MapMetaDTO(name, width, height, startingPositions);
        }
        throw new IllegalArgumentException("Major failure");
    }
    public static Map loadMap (Engine engine, String mapName) {
        List<String> availableMaps = MapLoader.getAvailableMaps();
        if (!availableMaps.contains(mapName)) {
            throw new RuntimeException("Map " + mapName + " not found.");
        }
        Class<?> mapClass = null;
        Object mapInstance = null;
        try {
            mapClass = Class.forName(Path.MAPS_PACKAGE + mapName);
            mapInstance = mapClass.getConstructor(Engine.class).newInstance(engine);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to instantiate " + mapName + ".");
        }
        /* THE ROOT OF ALL THE EVIL, BE CAUTIOUS! */
        if (mapInstance instanceof Map) {
            return (Map)mapInstance;
        }
        throw new RuntimeException("\"" + mapName + "\" is not a map.");
    }
    public static Map loadSimpleMap (Engine engine, String mapName) {
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
        return new SimpleMap(engine, MapLoader.retrieveMapMetaData(mapFile), mapFile);
    }    
}
