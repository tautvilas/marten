package marten.aoe.dto;

import java.io.Serializable;

import marten.aoe.data.type.UnitType;
import marten.aoe.data.units.Units;

public final class UnitDTO implements Serializable {
    private static final long serialVersionUID = 8927124760274010740L;
    private final Units name;
    private final PlayerDTO player;
    private final int hitPoints;
    private final int maxHitPoints;
    private final int movePoints;
    private final int maxMovePoints;
    private final int id;
    private final boolean cloaked;
    private UnitType type;

    public UnitDTO (int id, Units name, PlayerDTO player, int hitPoints, int maxHitPoints, int movePoints, int maxMovePoints, boolean cloaked, UnitType type) {
        this.name = name;
        this.id = id;
        this.player = player;
        this.hitPoints = hitPoints;
        this.maxHitPoints = maxHitPoints;
        this.movePoints = movePoints;
        this.maxMovePoints = maxMovePoints;
        this.cloaked = cloaked;
        this.type = type;
    }
    public int getId() {
        return this.id;
    }
    public final Units getName () {
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
    public final boolean getCloaked () {
        return this.cloaked;
    }
    public final UnitType getType() {
        return this.type;
    }
}
