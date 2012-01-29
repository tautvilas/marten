package marten.aoe.data.units;

import marten.aoe.data.type.DamageType;
import marten.aoe.data.type.UnitSize;
import marten.aoe.data.type.UnitType;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.depreciated.DamageDTO;
import marten.aoe.dto.depreciated.DamageResistanceDTO;
import marten.aoe.engine.core.Regiment;
import marten.aoe.engine.core.Tile;
import marten.aoe.engine.core.Unit;

public final class Human extends Regiment {
    public Human(Human other, Tile location) {
        super (other, location);
    }
    public Human(PlayerDTO owner, Tile location) {
        super("Human", location, owner, UnitSize.SMALL, UnitType.GROUND, 6, 8, 6, 0, new DamageDTO(6, DamageType.MECHANICAL), null, 0);
    }
    @Override public Unit clone(Tile location) {
        return new Human(this, location);
    }
    @Override public DamageResistanceDTO getDamageResistance() {
        return new DamageResistanceDTO(new int[] {+3, 0, 0, 0, 0, 0, 0, 0});
    }
    @Override public String[] getSpecialFeatures() {
        return new String[] {"Heavy armour", "Shield", "Sword"};
    }
    @Override public void onDeath() {}
    @Override public void onTileEntry(Tile tile) {}
    @Override public void onTileExit(Tile tile) {}
    @Override public void onTurnOver() {}
    @Override public boolean isCloaked() {
        return false;
    }
    @Override public boolean isObserving() {
        return false;
    }
}
