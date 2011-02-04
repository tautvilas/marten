package marten.aoe.engine;

import java.util.HashMap;
import java.util.List;

public final class Player {
    public static final Player SYSTEM = new Player(0);
    private final int team;
    public Player (int team) {
        this.team = team;
    }
    public final int getTeam() {
        return this.team;
    }
    public final List<Unit> getAllUnits() {
        // TODO: implement
        return null;
    }
    @Deprecated public final HashMap<String, Unit> getAllUnitTypes() {
        // FIXME: For testing purposes only. In normal circumstances the players should
        // rely on buildings and/or map events to get new units.
        // TODO: implement
        return null;
    }
}
