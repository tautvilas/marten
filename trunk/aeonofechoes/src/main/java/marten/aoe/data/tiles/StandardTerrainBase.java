package marten.aoe.data.tiles;

import java.util.EnumMap;

import marten.aoe.proposal.dto.DamageDTO;
import marten.aoe.proposal.dto.DefenseDTO;
import marten.aoe.proposal.dto.MovementDTO;
import marten.aoe.proposal.dto.PointDTO;
import marten.aoe.proposal.dto.UnitSize;
import marten.aoe.proposal.dto.UnitType;
import marten.aoe.proposal.engine.Map;
import marten.aoe.proposal.engine.TileBase;

/** Convenience class to help define most of the terrain types succinctly. */
public abstract class StandardTerrainBase extends TileBase {
    private final int groundMovementCost;
    private final DamageDTO entryDamage;
    private final DefenseDTO defense;
    private final MovementDTO movement;
    public StandardTerrainBase(String name, Map owner, PointDTO coordinates, int groundMovementCost, DamageDTO entryDamage) {
        super(name, owner, coordinates);
        this.groundMovementCost = groundMovementCost;
        this.entryDamage = entryDamage;
        // Forming defense
        EnumMap<UnitType, EnumMap<UnitSize, Integer>> data = new EnumMap<UnitType, EnumMap<UnitSize, Integer>>(UnitType.class);
        for (UnitType type : UnitType.values()) {
            EnumMap<UnitSize, Integer> data2 = new EnumMap<UnitSize, Integer>(UnitSize.class);
            for (UnitSize size : UnitSize.values()) {
                data2.put(size, 0);
            }
            data.put(type, data2);
        }
        this.defense = new DefenseDTO(data);
        // Forming movement
        data = new EnumMap<UnitType, EnumMap<UnitSize, Integer>>(UnitType.class);
        for (UnitType type : UnitType.values()) {
            EnumMap<UnitSize, Integer> data2 = new EnumMap<UnitSize, Integer>(UnitSize.class);
            for (UnitSize size : UnitSize.values()) {
                switch (type) {
                    case GROUND:
                        data2.put(size, this.groundMovementCost);
                        break;
                    case AERIAL:
                        data2.put(size, 1);
                        break;
                    case MARINE:
                        data2.put(size, Integer.MAX_VALUE);
                }
            }
            data.put(type, data2);
        }
        this.movement = new MovementDTO(data);
    }
    @Override public final DefenseDTO getDefenseBonus() {
        return this.defense;
    }
    @Override public final int getHeight() {
        return 0;
    }
    @Override public final MovementDTO getMovementCost() {
        return this.movement;
    }
    @Override public final String[] getSpecialFeatures() {
        if (this.groundMovementCost > 1) {
            return new String[] {"Difficult terrain"};
        }
        return new String[] {"Normal terrain"};
    }
    @Override public final void onTurnOver() {
    }
    @Override public final void onUnitEntry() {
        if (this.entryDamage != null && this.getUnit().getUnitType() == UnitType.GROUND) {
            this.getUnit().applyDamage(this.entryDamage);
        }
    }
    @Override public final void onUnitExit() {        
    }
}
