package marten.aoe.proposal.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import marten.aoe.proposal.dto.MapDTO;
import marten.aoe.proposal.dto.MinimalMapDTO;
import marten.aoe.proposal.dto.MinimalTileDTO;
import marten.aoe.proposal.dto.MinimalUnitDTO;
import marten.aoe.proposal.dto.Point;
import marten.aoe.proposal.dto.TileDTO;
import marten.aoe.proposal.dto.UnitDTO;

public final class Engine {
    private static final String MAPS_PATH = "src/main/java/marten/aoe/proposal/maps/";
    private static final String MAPS_PACKAGE = "marten.aoe.proposal.maps";    
    private Map map = null;
    public static final List<String> getAvailableMaps () {
        File mapDirectory = new File(MAPS_PATH);
        String[] mapList = mapDirectory.list();
        List<String> mapNameList = new ArrayList<String>();
        for (String mapName : mapList) {
            if (mapName.matches(".*\\.java")) {
                mapNameList.add(mapName.substring(0, mapName.length() - 5));
            }
        }
        return mapNameList;
    }
    public boolean loadMap (String mapName) {
        List<String> availableMaps = getAvailableMaps();
        if (!availableMaps.contains(mapName))
            return false;
        Class<?> mapClass = null;
        Object mapInstance = null;
        try {
            mapClass = Class.forName(MAPS_PACKAGE + mapName);
            mapInstance = mapClass.newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        /* THE ROOT OF ALL THE EVIL, BE CAUTIOUS! */
        if (mapInstance instanceof Map) {
            this.map = (Map)mapInstance;
            return true;
        }
        return false;
    }
    public MinimalMapDTO getMinimalMapDTO () {        
        return (this.map != null) ? this.map.getMinimalDTO() : null;
    }
    public MapDTO getMapDTO () {
        return (this.map != null) ? this.map.getDTO() : null;
    }
    public MinimalTileDTO getMinimalTileDTO (Point location) {
        return (this.map != null) ? this.map.getTile(location).getMinimalDTO() : null;
    }
    public TileDTO getTileDTO (Point location) {
        return (this.map != null) ? this.map.getTile(location).getDTO() : null;
    }
    public MinimalUnitDTO getMinimalUnitDTO (Point location) {
        return (this.map != null) ? this.map.getTile(location).getUnit().getMinimalDTO() : null;
    }
    public UnitDTO getUnitDTO (Point location) {
        return (this.map != null) ? this.map.getTile(location).getUnit().getDTO() : null;
    }
    public void endTurn () {
        if (this.map != null) {
            this.map.endTurn();
        }
    }
}
