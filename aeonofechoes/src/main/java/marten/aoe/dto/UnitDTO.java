package marten.aoe.dto;

import java.io.Serializable;

public final class UnitDTO implements Serializable {
    private static final long serialVersionUID = 8927124760274010740L;
    private final String name;
    private final PlayerDTO player;
    public UnitDTO (String name, PlayerDTO player) {
        this.name = name;
        this.player = player;
    }
    public final String getName () {
        return this.name;
    }
    public final PlayerDTO getOwner () {
        return this.player;
    }
}
