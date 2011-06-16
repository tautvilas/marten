package marten.aoe.engine.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import marten.aoe.Path;
import marten.aoe.engine.Engine;
import marten.aoe.engine.Map;

public final class MapLoader {
    public static final List<String> getAvailableMaps () {
        File mapDirectory = new File(Path.MAP_PATH);
        String[] mapList = mapDirectory.list();
        List<String> mapNameList = new ArrayList<String>();
        for (String mapName : mapList) {
            if (mapName.matches(".*\\.class")) {
                mapNameList.add(mapName.split("\\.")[0]);
            }
        }
        return mapNameList;
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
}