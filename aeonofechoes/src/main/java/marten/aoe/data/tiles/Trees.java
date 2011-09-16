package marten.aoe.data.tiles;

import java.util.EnumMap;

import marten.aoe.dto.DefenseDTO;
import marten.aoe.dto.MovementDTO;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.UnitSize;
import marten.aoe.dto.UnitType;
import marten.aoe.engine.Tile;
import marten.aoe.engine.TileLayer;

public final class Trees extends TileLayer {

    public Trees(Tile base) {
        super("Trees", base);
        String[] features = base.getSpecialFeatures();
        for (String feature : features) {
            if (feature.contains("Water")) {
                throw new RuntimeException("Illegal placement of trees");
            }
        }
    }

    @Override public String[] getLayerSpecificSpecialFeatures() {
        return new String[] {"Obstacles"};
    }

    @Override public boolean isCloaked(PlayerDTO player) {
        return false;
    }

    @Override public void onTurnOver() {        
    }

    @Override public DefenseDTO getDefenseBonus() {
        DefenseDTO baseDefense = this.getBase().getDefenseBonus();
        EnumMap<UnitType, EnumMap<UnitSize, Integer>> data = new EnumMap<UnitType, EnumMap<UnitSize, Integer>>(UnitType.class);
        for (UnitType type : UnitType.values()) {
            EnumMap<UnitSize, Integer> data2 = new EnumMap<UnitSize, Integer>(UnitSize.class);
            for (UnitSize size : UnitSize.values()) {
                if (type != UnitType.GROUND) {
                    data2.put(size, baseDefense.getValue(size, type));
                } else {
                    switch (size) {
                    case SMALL:
                        data2.put(size, baseDefense.getValue(size, type) - 2);
                        break;
                    case MEDIUM:
                        data2.put(size, baseDefense.getValue(size, type) - 1);
                        break;
                    default:
                        data2.put(size, baseDefense.getValue(size, type));
                    }
                }
            }
            data.put(type, data2);
        }
        return new DefenseDTO(data);
    }

    @Override public int getHeight() {
        return this.getBase().getHeight();
    }

    @Override
    public MovementDTO getMovementCost() {
        MovementDTO baseMovement = this.getBase().getMovementCost();
        EnumMap<UnitType, EnumMap<UnitSize, Integer>> data = new EnumMap<UnitType, EnumMap<UnitSize, Integer>>(UnitType.class);
        for (UnitType type : UnitType.values()) {
            EnumMap<UnitSize, Integer> data2 = new EnumMap<UnitSize, Integer>(UnitSize.class);
            for (UnitSize size : UnitSize.values()) {
                if (type != UnitType.GROUND) {
                    data2.put(size, baseMovement.getValue(size, type));
                } else {
                    switch (size) {
                    case SMALL:
                        data2.put(size, baseMovement.getValue(size, type));
                        break;
                    case MEDIUM:
                        data2.put(size, baseMovement.getValue(size, type) + 1);
                        break;
                    default:
                        data2.put(size, baseMovement.getValue(size, type) + 2);
                    }
                }
            }
            data.put(type, data2);
        }
        return new MovementDTO(data);
    }

    @Override public void onUnitEntry() {
    }

    @Override public void onUnitExit() {
    }

}
