package marten.aoe.dto;

import java.io.Serializable;

public final class UnitDTO implements Serializable {
    private static final long serialVersionUID = 8927124760274010740L;
    private final String name;
    private final PlayerDTO player;
    private final int hitPoints;
    private final int maxHitPoints;
    private final int movePoints;
    private final int maxMovePoints;
    public UnitDTO (String name, PlayerDTO player, int hitPoints, int maxHitPoints, int movePoints, int maxMovePoints) {
        this.name = name;
        this.player = player;
        this.hitPoints = hitPoints;
        this.maxHitPoints = maxHitPoints;
        this.movePoints = movePoints;
        this.maxMovePoints = maxMovePoints;
    }
    public final String getName () {
        return this.name;
    }
    public final PlayerDTO getOwner () {
        return this.player;
    }
    public final int getCurrentHitPoints () {
        return this.hitPoints;
    }
    public final int getMaximumHitPoints () {
        return this.maxHitPoints;
    }
    public final int getCurrentMovementPoints () {
        return this.movePoints;
    }
    public final int getMaximumMovementPoints () {
        return this.maxMovePoints;
    }
}
