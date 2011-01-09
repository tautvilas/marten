package marten.aoe.proposal.engine;

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
}
