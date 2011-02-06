package marten.aoe.data.units;

import marten.aoe.dto.DamageDTO;
import marten.aoe.dto.DamageResistanceDTO;
import marten.aoe.dto.DamageType;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.UnitSize;
import marten.aoe.dto.UnitType;
import marten.aoe.engine.Player;
import marten.aoe.engine.Regiment;
import marten.aoe.engine.Tile;
import marten.aoe.engine.Unit;

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
    @Override public void specialAction4(PointDTO target) {}
    @Override public void specialAction5(PointDTO target) {}
    @Override public void specialAction6(PointDTO target) {}
    @Override public void specialAction7(PointDTO target) {}
    @Override public void specialAction8(PointDTO target) {}
    @Override public void specialAction9(PointDTO target) {}
}
