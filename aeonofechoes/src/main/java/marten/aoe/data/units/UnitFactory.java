package marten.aoe.data.units;

import org.apache.log4j.Logger;

public class UnitFactory {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(UnitFactory.class);

    public static UnitDetails getUnit(String id) {
        UnitDetails unit = new UnitDetails(id);
        log.error("Unit " + id + " not defined");
        return unit;
    }
}
