package marten.aoe.proposal.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import marten.aoe.Path;
import marten.aoe.proposal.engine.Map;

import org.apache.log4j.Logger;

public final class MapLoader {
    private static org.apache.log4j.Logger log = Logger.getLogger(MapLoader.class);
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
    public static Map loadMap (String mapName) {
        List<String> availableMaps = getAvailableMaps();
        if (!availableMaps.contains(mapName)) {
            log.error("Map '" + mapName + "' was not found");
            return null;
        }
        Class<?> mapClass = null;
        Object mapInstance = null;
        try {
            mapClass = Class.forName(Path.MAPS_PACKAGE + mapName);
            mapInstance = mapClass.newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        /* THE ROOT OF ALL THE EVIL, BE CAUTIOUS! */
        if (mapInstance instanceof Map) {
            return (Map)mapInstance;
        }
        return null;
    }
}