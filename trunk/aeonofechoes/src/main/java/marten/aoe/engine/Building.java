package marten.aoe.engine;

import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.UnitSize;
import marten.aoe.dto.UnitType;

/** Extension point for most of the buildings in the game.
 * @author Petras Ražanskas */
public abstract class Building extends Unit {
    /** Creates a copy of another building in the given location.
     * @param other - the building being copied.
     * @param location - the location where the new building will be.*/
    public Building(Building other, Tile location) {
        super(other, location);
    }
    /** Creates a new building with the given parameters.
     * @param name - the name of the building
     * @param location - the location where the new building will be.
     * @param owner - the player to which this building will respond.
     * @param hitPoints - the number of hits the building will suffer before collapsing.*/
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
