package marten.aoe.data.tiles;

import java.util.EnumMap;

import marten.aoe.dto.DefenseDTO;
import marten.aoe.dto.MovementDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.UnitSize;
import marten.aoe.dto.UnitType;
import marten.aoe.engine.Map;
import marten.aoe.engine.TileBase;

public final class Water extends TileBase {
    private final DefenseDTO defense;
    private final MovementDTO movement;
    public Water(Map owner, PointDTO coordinates) {
        super("Water", owner, coordinates);
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
                        data2.put(size, Integer.MAX_VALUE);
                        break;
                    case AERIAL:
                        data2.put(size, 1);
                        break;
                    case MARINE:
                        data2.put(size, 1);
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
        return new String[] {"Water"};
    }
    @Override public final void onTurnOver() {
    }
    @Override public final void onUnitEntry() {
    }
    @Override public final void onUnitExit() {        
    }
}
