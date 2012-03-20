package marten.aoe.data.units;

import marten.aoe.data.type.UnitType;

import org.apache.log4j.Logger;

public class UnitFactory {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(UnitFactory.class);

    public static UnitDetails getUnit(Units id) {
        UnitDetails unit = null;
        if (id == Units.BASE) {
            unit = new UnitDetails(id, UnitType.BUILDING);
        } else {
            log.error("Unit " + id + " not defined");
            unit = new UnitDetails(id);
        }
        return unit;
    }
}
