package marten.aoe.proposal.tiles;

import java.util.EnumMap;

import marten.aoe.proposal.dto.DamageDTO;
import marten.aoe.proposal.dto.DefenseDTO;
import marten.aoe.proposal.dto.MovementDTO;
import marten.aoe.proposal.dto.Point;
import marten.aoe.proposal.dto.UnitSize;
import marten.aoe.proposal.dto.UnitType;
import marten.aoe.proposal.engine.Map;
import marten.aoe.proposal.engine.TileBase;

public final class Swamp extends TileBase {
    private final DefenseDTO defense;
    private final MovementDTO movement;
    public Swamp(String name, Map owner, Point coordinates, int groundMovementCost, DamageDTO entryDamage) {
        super(name, owner, coordinates);
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
                        data2.put(size, 3);
                        break;
                    case AERIAL:
                        data2.put(size, 1);
                        break;
                    case MARINE:
                        data2.put(size, 3);
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
        return new String[] {"Difficult terrain", "Shallow water"};        
    }
    @Override public final void onTurnOver() {
    }
    @Override public final void onUnitEntry() {        
    }
    @Override public final void onUnitExit() {        
    }
}
