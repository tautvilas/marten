package marten.aoe.data.units;

import marten.aoe.dto.Action;
import marten.aoe.dto.DamageDTO;
import marten.aoe.dto.DamageResistanceDTO;
import marten.aoe.dto.DamageType;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.UnitSize;
import marten.aoe.dto.UnitType;
import marten.aoe.engine.Regiment;
import marten.aoe.engine.Tile;
import marten.aoe.engine.Unit;

public final class Elf extends Regiment {
    public Elf(Elf other, Tile location) {
        super (other, location);
    }
    public Elf(PlayerDTO owner, Tile location) {
        super("Elf", location, owner, UnitSize.SMALL, UnitType.GROUND, 7, 5, 7, 0, new DamageDTO(6, DamageType.MECHANICAL), new DamageDTO(6, DamageType.MECHANICAL), 3);
    }
    @Override public Unit clone(Tile location) {
        return new Elf(this, location);
    }
    @Override public DamageResistanceDTO getDamageResistance() {
        return new DamageResistanceDTO(new int[] {0, 0, 0, 0, 0, 0, +1, +1});
    }
    @Override public String[] getSpecialFeatures() {
        return new String[] {"Frail", "Light armour", "Sword", "Long bow", "Calm", "Resistant to magic"};
    }
    @Override public void onDeath() {}
    @Override public void onTileEntry(Tile tile) {}
    @Override public void onTileExit(Tile tile) {}
    @Override public void onTurnOver() {}
    @Override public void extendedSpecialAction(PointDTO target, Action action) {}
}
