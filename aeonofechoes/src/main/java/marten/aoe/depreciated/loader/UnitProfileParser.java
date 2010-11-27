package marten.aoe.depreciated.loader;

import marten.aoe.depreciated.engine.Engine;
import marten.aoe.depreciated.engine.UnitProfile;
import marten.aoe.depreciated.engine.UnitType;

final class UnitProfileParser {
    public static void parse(Engine engine, DataTree branch) {
        if (branch.value().equals("UnitProfile")) {
            String name = "";
            int maxMovement = 0;
            UnitType type = null;
            for (DataTree subbranch : branch.branches()) {
                if (subbranch.value().equals("KEYVALUE")) {
                    String key = subbranch.branches().get(0).value();
                    String value = subbranch.branches().get(1).value();
                    if (key.equals("Name"))
                        name = value;
                    if (key.equals("Movement"))
                        maxMovement = Integer.parseInt(value);
                    if (key.equals("Type"))
                        type = UnitType.fromString(value);
                }                    
            }
            new UnitProfile(engine.unitProfiles, name, type, maxMovement);
        }
    }
}
