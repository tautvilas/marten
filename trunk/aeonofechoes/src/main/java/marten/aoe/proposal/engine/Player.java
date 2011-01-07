package marten.aoe.proposal.engine;

public final class Player {
    public static final Player SYSTEM = new Player(0);
    private final int team;
    public Player (int team) {
        this.team = team;
    }
    public final int getTeam() {
        return this.team;
    }
}
