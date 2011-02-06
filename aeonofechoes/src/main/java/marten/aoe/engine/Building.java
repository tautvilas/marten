package marten.aoe.engine;

import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.UnitSize;
import marten.aoe.dto.UnitType;

/** Extension point for most of the buildings in the game. */
public abstract class Building extends Unit {
    public Building(Building other, Tile location) {
        super(other, location);
    }
    public Building(String name, Tile location, PlayerDTO owner, int hitPoints) {
        super(
                name,
                location,
                owner,
                UnitSize.LARGE,
                UnitType.GROUND,
                0,
                hitPoints,
                1,
                0
        );
    }
}
