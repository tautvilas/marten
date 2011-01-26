package marten.aoe.proposal.tiles;

import java.util.EnumMap;

import marten.aoe.proposal.dto.DefenseDTO;
import marten.aoe.proposal.dto.MovementDTO;
import marten.aoe.proposal.dto.PointDTO;
import marten.aoe.proposal.dto.UnitSize;
import marten.aoe.proposal.dto.UnitType;
import marten.aoe.proposal.engine.Map;
import marten.aoe.proposal.engine.TileBase;

public final class Void extends TileBase {
    private final DefenseDTO defense;
    private final MovementDTO movement;
    public Void(Map owner, PointDTO coordinates) {
        super("Void", owner, coordinates);
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
                data2.put(size, Integer.MAX_VALUE);
            }
            data.put(type, data2);
        }
        this.movement = new MovementDTO(data);
    }
    @Override public final DefenseDTO getDefenseBonus() {
        return this.defense;
    }
    @Override public int getHeight() {
        return 0;
    }
    @Override public final MovementDTO getMovementCost() {
        return this.movement;
    }
    @Override public String[] getSpecialFeatures() {
        return new String[] {"Impassable"};
    }
    @Override public void onTurnOver() {        
    }
    @Override public void onUnitEntry() {
    }
    @Override public void onUnitExit() {
    }
}
