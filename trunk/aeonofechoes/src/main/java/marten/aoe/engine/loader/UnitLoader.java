package marten.aoe.engine.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import marten.aoe.Path;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.engine.Tile;
import marten.aoe.engine.Unit;

import org.apache.log4j.Logger;

public final class UnitLoader {
    private static org.apache.log4j.Logger log = Logger.getLogger(UnitLoader.class);
    public static final List<String> getAvailableUnits () {
        File unitDirectory = new File(Path.UNIT_PATH);
        String[] unitList = unitDirectory.list();
        List<String> unitNameList = new ArrayList<String>();
        for (String unitName : unitList) {
            if (unitName.matches(".*\\.class")) {
                unitNameList.add(unitName.split("\\.")[0]);
            }
        }
        return unitNameList;
    }
    public static Unit loadUnit (String unitName, PlayerDTO owner, Tile location) {
        List<String> availableUnits = UnitLoader.getAvailableUnits();
        if (!availableUnits.contains(unitName)) {
            UnitLoader.log.warn(unitName + " not found.");
            return null;
        }
        Class<?> unitClass = null;
        Object unitInstance = null;
        try {
            unitClass = Class.forName(Path.UNIT_PACKAGE + unitName);
            unitInstance = unitClass.getConstructor(PlayerDTO.class, Tile.class).newInstance(owner, location);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        /* THE ROOT OF ALL THE EVIL, BE CAUTIOUS! */
        if (unitInstance instanceof Unit) {
            return (Unit)unitInstance;
        }
        return null;
    }
}
