package marten.aoe.data.tiles;

import java.util.EnumMap;

import marten.aoe.dto.DefenseDTO;
import marten.aoe.dto.MovementDTO;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.UnitSize;
import marten.aoe.dto.UnitType;
import marten.aoe.engine.Tile;
import marten.aoe.engine.TileLayer;

public final class Hill extends TileLayer {

    public Hill(Tile base) {
        super("Hill", base);
        String[] features = base.getSpecialFeatures();
        for (String feature : features) {
            if (feature.contains("Water") || feature.contains("water")) {
                throw new RuntimeException("Illegal placement of a hill");
            }
        }
    }

    @Override public String[] getLayerSpecificSpecialFeatures() {
        return new String[] {"Hill"};
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
                data2.put(size, baseDefense.getValue(size, type) + (type == UnitType.GROUND ? -1 : 0));
            }
            data.put(type, data2);
        }
        return new DefenseDTO(data);
    }

    @Override public int getHeight() {
        return 1;
    }

    @Override public MovementDTO getMovementCost() {
        MovementDTO baseMovement = this.getBase().getMovementCost();
        EnumMap<UnitType, EnumMap<UnitSize, Integer>> data = new EnumMap<UnitType, EnumMap<UnitSize, Integer>>(UnitType.class);
        for (UnitType type : UnitType.values()) {
            EnumMap<UnitSize, Integer> data2 = new EnumMap<UnitSize, Integer>(UnitSize.class);
            for (UnitSize size : UnitSize.values()) {
                if (baseMovement.getValue(size, type) == 0) {
                    data2.put(size, 0);
                } else {                    
                    switch (type) {
                    case GROUND:
                        data2.put(size, baseMovement.getValue(size, type) + 1);
                        break;
                    case AERIAL:
                        data2.put(size, baseMovement.getValue(size, type));
                        break;
                    case MARINE:
                        data2.put(size, 0);
                        break;
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
