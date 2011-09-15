package marten.aoe.engine.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import marten.aoe.Path;
import marten.aoe.data.tiles.Generic;
import marten.aoe.dto.PointDTO;
import marten.aoe.engine.Map;
import marten.aoe.engine.Tile;

import org.apache.log4j.Logger;

public final class TileLoader {
    private static org.apache.log4j.Logger log = Logger.getLogger(TileLoader.class);    
    public static final List<String> getAvailableTiles () {
        File tileDirectory = new File(Path.TILE_PATH);
        String[] tileList = tileDirectory.list();
        List<String> tileNameList = new ArrayList<String>();
        for (String tileName : tileList) {
            if (tileName.matches(".*\\.class")) {
                tileNameList.add(tileName.split("\\.")[0]);
            }
        }
        return tileNameList;
    }
    public static Tile loadTile (String tileName, Map owner, PointDTO location) {
        List<String> availableTiles = getAvailableTiles();
        if (!availableTiles.contains(tileName)) {
            log.warn(tileName + " not found.");
            return new Generic(tileName, owner, location);
        }
        Class<?> tileClass = null;
        Object tileInstance = null;
        try {
            tileClass = Class.forName(Path.TILE_PACKAGE + tileName);
            tileInstance = tileClass.getConstructor(Map.class, PointDTO.class).newInstance(owner, location);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        /* THE ROOT OF ALL THE EVIL, BE CAUTIOUS! */
        if (tileInstance instanceof Tile) {
            return (Tile)tileInstance;
        }
        return null;
    }
    public static Tile loadLayer (String layerName, Tile tile) {
        List<String> availableTiles = getAvailableTiles();
        if (!availableTiles.contains(layerName)) {
            log.warn(layerName + " not found.");
            /*
            log.debug("Detected tile types are:");
            for (String availableTile : availableTiles) {
                log.debug("\t"+availableTile);
            }
            */
            return tile;
        }
        Class<?> tileClass = null;
        Object tileInstance = null;
        try {
            tileClass = Class.forName(Path.TILE_PACKAGE + layerName);
            tileInstance = tileClass.getConstructor(Tile.class).newInstance(tile);
        }
        catch (Exception e) {
            e.printStackTrace();
            return tile;
        }
        /* THE ROOT OF ALL THE EVIL, BE CAUTIOUS! */
        if (tileInstance instanceof Tile) {
            return (Tile)tileInstance;
        }        
        return tile;
    }
}
