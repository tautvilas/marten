package marten.aoe.data.units;

import marten.aoe.data.type.DamageType;
import marten.aoe.data.type.UnitSize;
import marten.aoe.data.type.UnitType;
import marten.aoe.dto.depreciated.DamageDTO;
import marten.aoe.dto.depreciated.DamageResistanceDTO;
import marten.aoe.engine.core.Player;
import marten.aoe.engine.core.Regiment;
import marten.aoe.engine.core.Tile;
import marten.aoe.engine.core.Unit;

public final class Dwarf extends Regiment {
    public Dwarf(Dwarf other, Tile location) {
        super (other, location);
    }
    public Dwarf(Player owner, Tile location) {
        super("Dwarf", location, owner, UnitSize.SMALL, UnitType.GROUND, 5, 8, 5, 0, new DamageDTO(6, DamageType.MECHANICAL), null, 0);
    }
    @Override public Unit clone(Tile location) {
        return new Dwarf(this, location);
    }
    @Override public DamageResistanceDTO getDamageResistance() {
        return new DamageResistanceDTO(new int[] {+4, -1, 0, 0, 0, -2, +1, +3});
    }
    @Override public String[] getSpecialFeatures() {
        return new String[] {"Sturdy", "Heavy armour", "Shield", "Hammer", "Native to cold", "Calm", "Immune to magic"};
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
