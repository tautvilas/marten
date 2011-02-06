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

public final class Human extends Regiment {
    public Human(Human other, Tile location) {
        super (other, location);
    }
    public Human(Player owner, Tile location) {
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
    @Override public void specialAction4(PointDTO target) {}
    @Override public void specialAction5(PointDTO target) {}
    @Override public void specialAction6(PointDTO target) {}
    @Override public void specialAction7(PointDTO target) {}
    @Override public void specialAction8(PointDTO target) {}
    @Override public void specialAction9(PointDTO target) {}
}
