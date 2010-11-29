package marten.aoe.proposal.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import marten.aoe.Path;
import marten.aoe.proposal.dto.Point;
import marten.aoe.proposal.engine.Map;
import marten.aoe.proposal.engine.Tile;

public final class TileLoader {
    public static final List<String> getAvailableTiles () {
        File tileDirectory = new File(Path.TILE_PATH);
        String[] tileList = tileDirectory.list();
        List<String> tileNameList = new ArrayList<String>();
        for (String tileName : tileList) {
            if (tileName.matches(".*\\.class")) {
                tileNameList.add(tileName.substring(0, tileName.length() - 5));
            }
        }
        return tileNameList;
    }
    public static Tile loadTile (String tileName, Map owner, Point location) {
        List<String> availableTiles = getAvailableTiles();
        if (!availableTiles.contains(tileName))
            return null;
        Class<?> tileClass = null;
        Object tileInstance = null;
        try {
            tileClass = Class.forName(Path.TILE_PACKAGE + tileName);
            tileInstance = tileClass.getConstructor(Map.class, Point.class);
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
}
