package marten.aoe.proposal.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import marten.aoe.Path;
import marten.aoe.proposal.engine.Map;

public final class MapLoader {
    public static final List<String> getAvailableMaps () {
        File mapDirectory = new File(Path.MAP_PATH);
        String[] mapList = mapDirectory.list();
        List<String> mapNameList = new ArrayList<String>();
        for (String mapName : mapList) {
            if (mapName.matches(".*\\.class")) {
                mapNameList.add(mapName.substring(0, mapName.length() - 5));
            }
        }
        return mapNameList;
    }
    public static Map loadMap (String mapName) {
        List<String> availableMaps = getAvailableMaps();
        if (!availableMaps.contains(mapName))
            return null;
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
